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

public class UpdateUserEmailTest {
    UserRequests userRequests;
    CreateUser createUser;
    LoginUser loginUser;
    UpdateUserEmail updateUserEmail;

    @Before
    public void setUp() {
        userRequests = new UserRequests();
        createUser = new CreateUser("tropy7@yandex.ru", "1234567", "Mary");
        userRequests.create(createUser);
    }
    @Test
    @DisplayName("Update user email")
    public void updateUserEmail() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        updateUserEmail = new UpdateUserEmail("Tropi7@mail.ru");
        ValidatableResponse response1 = userRequests.updateEmail(accessToken, updateUserEmail);
        int statusCode = response1.extract().statusCode();
        boolean result = response1.extract().path("success");
        assertTrue(result);
        assertThat(statusCode, equalTo(SC_OK));
    }

    @After
    public void tearDown() {
        loginUser = new LoginUser("Tropi7@mail.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}