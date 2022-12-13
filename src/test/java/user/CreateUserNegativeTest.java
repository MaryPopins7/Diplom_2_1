package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

public class CreateUserNegativeTest {
    private static UserRequests userRequests;
    private static CreateUser createUser;
    public static LoginUser loginUser;


    @BeforeClass
    public static void setUp() {
        userRequests = new UserRequests();
        createUser = new CreateUser("tropy7@yandex.ru", "1234567", "Mary");
        userRequests.create(createUser);
    }

    @Test
    @DisplayName("Create existing user account")
    public void CreateExistingUserAccount() {
        createUser = new CreateUser("tropy7@yandex.ru", "1234567", "Mary");
        ValidatableResponse response = userRequests.create(createUser);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("User already exists", message);
        assertThat("User already exists", statusCode, equalTo(SC_FORBIDDEN));

    }
    @Test
    @DisplayName("Create user account without filling required fields(email)")
    public void CreateUserAccountWithoutEmail() {
        createUser = new CreateUser("", "1234567", "Mary");
        ValidatableResponse response = userRequests.create(createUser);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Email, password and name are required fields", message);
        assertThat("Email, password and name are required fields", statusCode, equalTo(SC_FORBIDDEN));
    }

    @Test
    @DisplayName("Create user account without filling required fields(password)")
    public void CreateUserAccountWithoutPassword() {
        createUser = new CreateUser("tropy7@yandex.ru", "", "Mary");
        ValidatableResponse response = userRequests.create(createUser);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Email, password and name are required fields", message);
        assertThat("Email, password and name are required fields", statusCode, equalTo(SC_FORBIDDEN));
    }

    @Test
    @DisplayName("Create user account without filling required fields(name)")
    public void CreateUserAccountWithoutName() {
        createUser = new CreateUser("tropy7@yandex.ru", "1234567", "");
        ValidatableResponse response = userRequests.create(createUser);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Email, password and name are required fields", message);
        assertThat("Email, password and name are required fields", statusCode, equalTo(SC_FORBIDDEN));
    }

    @AfterClass
    public static void tearDown() {
        loginUser = new LoginUser("tropy7@yandex.ru", "1234567");
        ValidatableResponse response = userRequests.login(loginUser);
        String accessToken = response.extract().path("accessToken");
        userRequests.delete(accessToken);
    }
}