package org.example.base;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.example.api.specifications.RequestSpec;
import org.example.utils.PropertiesReader;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public abstract class BaseApiTest {

    protected static final String BASE_API_URL = PropertiesReader.get("api.baseUrl", "http://localhost:9091");

    @BeforeAll
    public static void setupApi() {
        RestAssured.baseURI = BASE_API_URL;
        System.out.println("========= API BaseTest setup completed =========");
    }

    protected RequestSpecification request() {
        return given().spec(RequestSpec.baseRequestSpec());
    }

    protected RequestSpecification withFields(String fields) {
        return given().spec(RequestSpec.withFields(fields));
    }
}