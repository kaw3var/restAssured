package org.example;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.example.dto.IssueDTO;
import org.example.dto.ProjectDTO;
import org.junit.jupiter.api.BeforeAll;

import static org.hamcrest.Matchers.*;

public class BaseTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:9091";
        RestAssured.useRelaxedHTTPSValidation();
        System.out.println("========= BaseTest setup completed =========");
    }

    static protected RequestSpecification request() {
        return RestAssured
                .given()
                .header("Authorization", "Bearer perm-YWRtaW4=.NDMtMA==.Ohnd7h2sKBXtuMRaPgfd1woEFmAG4M")
                .contentType("application/json")
                .log().ifValidationFails();
        // perm-YWRtaW4=.NDMtMA==.Ohnd7h2sKBXtuMRaPgfd1woEFmAG4M - pc
        // perm-YWRtaW4=.NDQtMA==.2tVNLGVkhOpOMaJYo7FNssrkPJvia4 laptop
    }

    public String createIssueAndGetId(String summary, String description, String projectId) {
        IssueDTO issueDTO = new IssueDTO(summary, description, new ProjectDTO(projectId));

        return request()
                .body(issueDTO)
                .when()
                .post("/api/issues")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }

}
