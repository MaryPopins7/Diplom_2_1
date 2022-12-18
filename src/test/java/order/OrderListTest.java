package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import user.CreateUser;
import user.LoginUser;
import user.UserRequests;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

public class OrderListTest {
    private static UserRequests userRequests;
    private static LoginUser loginUser;
    private static OrderRequests orderRequests;
    IngredientsJson ingredients;

    @BeforeClass
    public static void setUp() {
        userRequests = new UserRequests();
        CreateUser createUser = new CreateUser("tropi7@yandex.ru", "1234567", "Mary");
        userRequests.create(createUser);
    }
    @Test
    @DisplayName("Get order list of login user")
    public void getOrderListWithLoginUser() {
        loginUser = new LoginUser("tropi7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        orderRequests = new OrderRequests();

        ValidatableResponse response1 = orderRequests.getIngredients();
        String ingredientHash = response1.extract().path("data[0]._id");
        ingredients = new IngredientsJson(ingredientHash);
        orderRequests.createOrder(accessToken, ingredients);

        ValidatableResponse responseOrder = orderRequests.getOrderWithLogin(accessToken);

        ArrayList<String> orderBody = responseOrder.extract().path("orders");
        assertNotEquals(Collections.EMPTY_LIST, orderBody);
        int statusCode = responseOrder.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }
    @Test
    @DisplayName("Get order list without login user")
    public void getOrderListWithoutLogin() {
        orderRequests = new OrderRequests();
        ValidatableResponse responseOrder = orderRequests.getOrderWithoutLogin();
        int statusCode = responseOrder.extract().statusCode();
        String message = responseOrder.extract().path("message");
        assertEquals("You should be authorised", message);
        assertThat("You should be authorised", statusCode, equalTo(SC_UNAUTHORIZED));

    }
    @AfterClass
    public static void tearDown() {
        loginUser = new LoginUser("tropi7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login( loginUser);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}