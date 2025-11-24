package org.example.api.specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.*;

public class ResponseSpec {

    public static ResponseSpecification cleanupSuccess() {
        return new ResponseSpecBuilder()
                .expectStatusCode(anyOf(is(200), is(400)))
                .build();
    }

    public static ResponseSpecification status200() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    public static ResponseSpecification status400BadRequest() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .build();
    }

    public static ResponseSpecification status404NotFound() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .build();
    }

    public static ResponseSpecification issueDetails(String expectedId, String expectedSummary, String expectedDescription) {
        return status200()
                .and()
                .body("id", equalTo(expectedId))
                .body("summary", equalTo(expectedSummary))
                .body("description", equalTo(expectedDescription));
    }

    public static ResponseSpecification issueUpdateSuccess(String updatedSummary, String updatedDescription) {
        return status200()
                .and()
                .body("summary", equalTo(updatedSummary))
                .body("description", equalTo(updatedDescription));
    }

    public static ResponseSpecification commentVerification(String commentId, String expectedCommentText) {
        return status200()
                .and()
                .body("find { it.id == '" + commentId + "' }.text", equalTo(expectedCommentText))
                .body("find { it.id == '" + commentId + "' }.author.name", notNullValue())
                .body("find { it.id == '" + commentId + "' }.created", greaterThan(0L));
    }


}
