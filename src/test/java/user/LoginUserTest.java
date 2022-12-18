package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.apache.http.HttpStatus.*;

public class LoginUserTest {
    private static UserRequests userRequests;
    public static CreateUser createUser;
    public static LoginUser loginUser;

    @BeforeClass
    public static void setUp() {
        userRequests = new UserRequests();
        createUser = new CreateUser("tropy7@yandex.ru", "1234567", "Mary");
        userRequests.create(createUser);
    }

    @Test
    @DisplayName("Successfully user login")
    public void successfullyUserLogin() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        int statusCode = response.extract().statusCode();
        boolean actual = response.extract().path("success");
        assertTrue(actual);
        assertThat("Success login, Enough data to login", statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Not enough data for user login")
    public void notEnoughDataForUserLogin() {
        loginUser = new LoginUser("", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("email or password are incorrect", message);
        assertThat("email or password are incorrect", statusCode, equalTo(SC_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Not enough data for user login without password")
    public void notEnoughDataForUserLoginWithoutPassword() {
        loginUser = new LoginUser("tropy7@yandex.ru", "");
        ValidatableResponse response = userRequests.login(loginUser);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("email or password are incorrect", message);
        assertThat("email or password are incorrect", statusCode, equalTo(SC_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Wrong password for user login")
    public void wrongDataForUserLogin() {
        loginUser = new LoginUser("tropy7@yandex.ru", "123");
        ValidatableResponse response = userRequests.login(loginUser);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("email or password are incorrect", message);
        assertThat("email or password are incorrect", statusCode, equalTo(SC_UNAUTHORIZED));
    }

    @AfterClass
    public static void tearDown() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}
