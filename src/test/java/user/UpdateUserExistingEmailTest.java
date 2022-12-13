package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class UpdateUserExistingEmailTest {
    UserRequests userRequests;
    LoginUser loginUser;
    UpdateUserEmail updateUserEmail;

    @Before
    public void setUp() {
        userRequests = new UserRequests();
        CreateUser createUser = new CreateUser("tropy7@yandex.ru", "1234567", "Mary");
        userRequests.create(createUser);
        CreateUser userCreateNew = new CreateUser("tropicana7@mail.ru", "1234567", "Mary");
        userRequests.create(userCreateNew);
    }

    @Test
    @DisplayName("Update existing user email")
    public void updateExistingUserEmail() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        updateUserEmail = new UpdateUserEmail("tropicana7@mail.ru");
        ValidatableResponse response1 = userRequests.updateEmail(accessToken, updateUserEmail);
        int statusCode = response1.extract().statusCode();
        boolean result = response1.extract().path("success");
        assertFalse(result);
        assertThat("User with such email already exists", statusCode, equalTo(SC_FORBIDDEN));
    }

    @After
    public void tearDown() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
        LoginUser userLoginNew = new LoginUser("tropicana7@mail.ru", "1234567");
        ValidatableResponse response1 = userRequests.login(userLoginNew);
        String accessToken1 = response1.extract().path("accessToken");
        userRequests.delete(accessToken1);
    }
}
