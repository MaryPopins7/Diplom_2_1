package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import user.CreateUser;
import user.LoginUser;
import user.UserRequests;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CreateOrderAuthUserTest {
    private static UserRequests userRequests;
    LoginUser loginUser;
    OrderRequests orderRequests;
    IngredientsJson ingredientsJson;

    @BeforeClass
    public static void setUp() {
        userRequests = new UserRequests();
        CreateUser createUser = new CreateUser("tropy7@yandex.ru", "1234567", "Mary");
        userRequests.create(createUser);
    }
    @Test
    @DisplayName("Create order with login")
    public void createOrderWithLoginUser() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");

        orderRequests = new OrderRequests();
        ValidatableResponse response1 = orderRequests.getIngredients();
        String ingredientHash = response1.extract().path("data[0]._id");
        ingredientsJson = new IngredientsJson(ingredientHash);

        ValidatableResponse response2 = orderRequests.createOrder(accessToken, ingredientsJson);
        int statusCode = response2.extract().statusCode();
        boolean result = response2.extract().path("success");
        assertTrue(result);
        assertThat(statusCode, equalTo(SC_OK));
    }
    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredients() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");

        orderRequests = new OrderRequests();
        ingredientsJson = new IngredientsJson();
        ValidatableResponse response2 = orderRequests.createOrder(accessToken, ingredientsJson);
        int statusCode = response2.extract().statusCode();
        boolean result = response2.extract().path("success");
        assertFalse(result);
        assertThat( "Ingredient ids must be provided", statusCode, equalTo(SC_BAD_REQUEST));
    }
    @Test
    @DisplayName("Create order with invalid ingredient hash")
    public void createOrderWithInvalidIngredient() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");

        orderRequests = new OrderRequests();
        String ingredientHash = "609646e4dc916e0027";
        ingredientsJson = new IngredientsJson(ingredientHash);

        ValidatableResponse response2 = orderRequests.createOrder(accessToken, ingredientsJson);
        int statusCode = response2.extract().statusCode();
        assertThat( "Internal Server Error", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }
    @AfterClass
    public static void tearDown() {
        LoginUser loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}