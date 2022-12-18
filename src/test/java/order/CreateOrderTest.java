package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import user.CreateUser;
import user.LoginUser;
import user.UserRequests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {
    private static UserRequests userRequests;
    IngredientsJson ingredientsJson;
    LoginUser loginUser;
    OrderRequests orderRequests;

    @BeforeClass
    public static void setUp() {
        userRequests = new UserRequests();
        CreateUser createUser = new CreateUser("tropy7@yandex.ru", "1234567", "Mary");
        userRequests.create(createUser);
    }
    @Test
    @DisplayName("Create order without user login")
    public void createOrderWithoutLoginUser() {
        orderRequests = new OrderRequests();
        ValidatableResponse response1 = orderRequests.getIngredients();
        String ingredientHash = response1.extract().path("data[0]._id");
        ingredientsJson = new IngredientsJson(ingredientHash);
        orderRequests.createOrderWithoutLogin(ingredientsJson);

        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        ValidatableResponse response2 = orderRequests.createOrder(accessToken, ingredientsJson);
        boolean result = response2.extract().path("success");
        int statusCode = response2.extract().statusCode();
        assertTrue(result);
        assertThat(statusCode, equalTo(SC_OK));
    }
    @Test
    @DisplayName("Create order without user login and without ingredients")
    public void createOrderWithoutLoginUserAndIngredients() {
        orderRequests = new OrderRequests();
        ingredientsJson = new IngredientsJson();
        orderRequests.createOrderWithoutIngredient();


        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        ValidatableResponse response1 = orderRequests.createOrder(accessToken, ingredientsJson);
        int statusCode = response1.extract().statusCode();
        boolean result = response1.extract().path("success");
        assertFalse(result);
        assertThat("Ingredient ids must be provided", statusCode, equalTo(SC_BAD_REQUEST));
    }
    @AfterClass
    public static void tearDown() {
        LoginUser loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}