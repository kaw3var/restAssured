package org.example.base;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.example.api.specifications.RequestSpec;
import org.example.dto.IssueDTO;
import org.example.dto.ProjectDTO;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static org.example.api.specifications.ResponseSpec.status200;
import static org.hamcrest.Matchers.notNullValue;

public class BaseTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:9091";
        System.out.println("========= BaseTest setup completed =========");
    }

    protected RequestSpecification request() {
        return given()
                .spec(RequestSpec.baseRequestSpec());

    }

    protected RequestSpecification withFields(String fields) {
        return given().spec(RequestSpec.withFields(fields));
    }


    public String createIssueAndGetId(String summary, String description, String projectId) {
        IssueDTO issueDTO = new IssueDTO(summary, description, new ProjectDTO(projectId));

        return request()
                .body(issueDTO)
                .when()
                .post("/issues")
                .then()
                .spec(status200())
                .body("id", notNullValue())
                .extract().path("id");
    }
}
