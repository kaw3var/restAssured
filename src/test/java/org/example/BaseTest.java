package org.example;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.example.dto.IssueDTO;
import org.example.dto.ProjectDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class BaseTest {

    static protected List<String> createdIssues = new ArrayList<>();

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

    @AfterEach
    void logTestResult(TestInfo info) {
        System.out.println("========= Test: " + info.getDisplayName() + " =========");
    }

   @AfterAll
    static void cleanUp() {
        for (String issuedId : createdIssues) {
            request()
                    .delete("/api/issues/" + issuedId)
                    .then()
                    .statusCode(anyOf(is(200), is(404)));
        }
        createdIssues.clear();
    }

    public String createIssueAndGetId(String summary, String description, String projectId) {
        IssueDTO issueDTO = new IssueDTO(summary, description, new ProjectDTO(projectId));

        String issueId = request()
                .body(issueDTO)
                .when()
                .post("/api/issues")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");

        createdIssues.add(issueId);
        return issueId;
    }

}
