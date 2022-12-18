package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CreateUserSuccessfullyTest {
    UserRequests userRequests;
    CreateUser createUser;

    @Before
    public void setUp() {
        userRequests = new UserRequests();
    }

    @Test
    @DisplayName("Create user account")
    public void CreateUserAccount() {
        createUser = new CreateUser("tropic7@yandex.ru", "1234567", "Mary");
        ValidatableResponse response = userRequests.create(createUser);
        String accessToken = response.extract().body().path("accessToken");
        int statusCode = response.extract().statusCode();
        boolean actual = response.extract().body().path("success");
        assertTrue(actual);
        assertThat(statusCode, equalTo(SC_OK));
        userRequests.delete(accessToken);
    }
}