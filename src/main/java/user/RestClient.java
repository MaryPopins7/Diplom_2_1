package user;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RestClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder().addHeader("Content-type", "application/json").setBaseUri(BASE_URL).build();
    }
}