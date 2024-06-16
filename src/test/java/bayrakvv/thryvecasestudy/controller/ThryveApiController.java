package bayrakvv.thryvecasestudy.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Base64;

import static bayrakvv.thryvecasestudy.secrets.Secrets.THRYVE_API_BASE_URL;
import static bayrakvv.thryvecasestudy.secrets.Secrets.USERNAME;
import static bayrakvv.thryvecasestudy.secrets.Secrets.PASSWORD;
import static bayrakvv.thryvecasestudy.secrets.Secrets.APP_AUTH_ID;
import static bayrakvv.thryvecasestudy.secrets.Secrets.APP_AUTH_SECRET;
import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

public class ThryveApiController {
    private static final String ENCODED_APP_CREDENTIALS =
        Base64
            .getEncoder()
            .encodeToString("%s:%s".formatted(APP_AUTH_ID, APP_AUTH_SECRET)
                .getBytes());
    private static final RequestSpecification SPEC = new RequestSpecBuilder()
        .setBaseUri(THRYVE_API_BASE_URL)
        .setAuth(basic(USERNAME, PASSWORD))
        .addHeader("AppAuthorization", "Basic %s".formatted(ENCODED_APP_CREDENTIALS))
        .build();

    public String getAccessToken() {
        return given()
            .spec(SPEC)
            .when()
            .post("/accessToken")
            .then()
            .statusCode(200)
            .extract()
            .response().asString();
    }

    public void uploadEpochDataFromWebApp(String accessToken, String body) {
        given()
            .spec(SPEC)
            .contentType(ContentType.JSON)
            .header("authenticationToken", accessToken)
            .body(body)
            .put("/dynamicEpochValue")
            .then()
            .statusCode(204);
    }

    public Response retrieveDataFromHealthDataApi(
        String accessToken, String startTimestamp, String endTimestamp) {
        return given()
            .spec(SPEC)
            .contentType(ContentType.URLENC)
            .formParam("authenticationToken", accessToken)
            .formParam("startTimestamp", startTimestamp)
            .formParam("endTimestamp", endTimestamp)
            .post("/dynamicEpochValues")
            .then()
            .statusCode(200)
            .extract()
            .response();
    }
}
