package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.RestClient;

import static io.restassured.RestAssured.given;

public class OrderRequests extends RestClient {
    public static final String GET_INGREDIENTS = "/api/ingredients";
    public static final String POST_AND_GET_ORDER = "/api/orders";

    @Step("Get order list of authorized user")
    public ValidatableResponse getOrderWithLogin(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(POST_AND_GET_ORDER)
                .then();
    }

    @Step("Create order without authorization")
    public ValidatableResponse createOrderWithoutLogin(IngredientsJson ingredients) {
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(POST_AND_GET_ORDER)
                .then();
    }

    @Step("Create order with authorized user and with ingredient")
    public ValidatableResponse createOrder(String accessToken, IngredientsJson ingredients) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(ingredients)
                .when()
                .post(POST_AND_GET_ORDER)
                .then();
    }

    @Step("Create order without ingredient")
    public ValidatableResponse createOrderWithoutIngredient() {
        return given()
                .spec(getBaseSpec())
                .when()
                .post(POST_AND_GET_ORDER)
                .then();
    }
    @Step("Get list of ingredient for getting hash")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_INGREDIENTS)
                .then();
    }

    @Step("Get order list without user login")
    public ValidatableResponse getOrderWithoutLogin() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(POST_AND_GET_ORDER)
                .then();
    }
}
