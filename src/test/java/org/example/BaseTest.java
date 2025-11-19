package org.example;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.example.dto.IssueDTO;
import org.example.dto.ProjectDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class BaseTest {

    protected static RequestSpecification request;
    protected List<String> createdIssues = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:9091";
        RestAssured.useRelaxedHTTPSValidation();

        request = RestAssured.given()
                .header("Authorization", "Bearer perm-YWRtaW4=.NDQtMA==.2tVNLGVkhOpOMaJYo7FNssrkPJvia4")
                .contentType("application/json")
                .log().ifValidationFails();

        System.out.println("========= BaseTest setup completed =========");
    }

    @AfterEach
    void logTestResult(TestInfo info) {
        System.out.println("========= Test: " + info.getDisplayName() + " =========");
    }

    @AfterEach
    void cleanUp() {
        for (String issuedId : createdIssues) {
            request
                    .delete("/api/issues/" + issuedId)
                    .then()
                    .statusCode(anyOf(is(200), is(404)));
        }
        createdIssues.clear();
    }

    public String createIssueAndGetId(String summary, String description, String projectId) {
        IssueDTO issueDTO = new IssueDTO(summary, description, new ProjectDTO(projectId));

        String issueId = request
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
