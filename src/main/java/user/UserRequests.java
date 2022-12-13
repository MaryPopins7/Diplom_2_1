package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserRequests extends RestClient {
    public static final String USER_REGISTER =  "/api/auth/register";
    public static final String USER_LOGIN =  "/api/auth/login";
    public static final String USER_DELETE_AND_PATCH = "/api/auth/user";

    @Step("Create user")
    public ValidatableResponse create(CreateUser userCreate) {
        return given()
                .spec(getBaseSpec())
                .body(userCreate)
                .when()
                .post(USER_REGISTER)
                .then();
    }
    @Step("User login")
    public ValidatableResponse login(LoginUser userLogin) {
        return given()
                .spec(getBaseSpec())
                .body(userLogin)
                .post(USER_LOGIN)
                .then();
    }
    @Step("Delete user")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_DELETE_AND_PATCH)
                .then();
    }
    @Step("Update user email")
    public ValidatableResponse updateEmail(String accessToken, UpdateUserEmail userUpdateEmail) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(userUpdateEmail)
                .when()
                .patch(USER_DELETE_AND_PATCH)
                .then();
    }
    @Step("Update user name")
    public ValidatableResponse updateName(String accessToken, UpdateUserName userUpdateName) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(userUpdateName)
                .when()
                .patch(USER_DELETE_AND_PATCH)
                .then();
    }
    @Step("Update unauthorized user name")
    public ValidatableResponse updateNameWithoutToken(UpdateUserName userUpdateName) {
        return given()
                .spec(getBaseSpec())
                .body(userUpdateName)
                .when()
                .patch(USER_DELETE_AND_PATCH)
                .then();
    }
    @Step("Update unauthorized user email")
    public ValidatableResponse updateEmailWithoutToken(UpdateUserEmail userUpdateEmail) {
        return given()
                .spec(getBaseSpec())
                .body(userUpdateEmail)
                .when()
                .patch(USER_DELETE_AND_PATCH)
                .then();
    }
}

