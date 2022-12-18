package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

public class UpdateUserUnauthorizedTest {
    private UserRequests userRequests;
    CreateUser createUser;
    UpdateUserEmail updateUserEmail;
    UpdateUserName updateUserName;
    LoginUser loginUser;


    @Before
    public void setUp() {
        userRequests = new UserRequests();
        createUser = new CreateUser("tropy7@yandex.ru", "1234567", "Mary");
        userRequests.create(createUser);
    }
    @Test
    @DisplayName("Update unauthorized user email")
    public void updateUnauthorizedUserEmail() {
        updateUserEmail = new UpdateUserEmail("Tropi@gmail.com");
        ValidatableResponse response = userRequests.updateEmailWithoutToken(updateUserEmail);
        int statusCode = response.extract().statusCode();
        boolean result = response.extract().path("success");
        assertFalse(result);
        assertThat( "You should be authorised", statusCode, equalTo(SC_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Update unauthorized user name")
    public void updateUnauthorizedUserName() {
        updateUserName = new UpdateUserName("Marina");
        ValidatableResponse response = userRequests.updateNameWithoutToken(updateUserName);
        int statusCode = response.extract().statusCode();
        boolean result = response.extract().path("success");
        assertFalse(result);
        assertThat( "You should be authorised", statusCode, equalTo(SC_UNAUTHORIZED));
    }

    @After
    public void tearDown() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}