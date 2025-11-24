package org.example.api.specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public class RequestSpec {

    private static final String TOKEN = "Bearer perm-YWRtaW4=.NDQtMA==.2tVNLGVkhOpOMaJYo7FNssrkPJvia4"; //laptop
//    private static final String TOKEN = "Bearer perm-YWRtaW4=.NDMtMA==.Ohnd7h2sKBXtuMRaPgfd1woEFmAG4M; //pc

    public static RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .setBasePath("/api")
                .addHeader("Authorization", TOKEN)
                .addHeader("Accept", "application/json")
                .setContentType("application/json")
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }


    public static RequestSpecification withFields(String fields) {
        return RestAssured
                .given()
                .spec(RequestSpec.baseRequestSpec())
                .queryParam("fields", fields);
    }
}
