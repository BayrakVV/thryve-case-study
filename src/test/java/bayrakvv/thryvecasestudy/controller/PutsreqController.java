package bayrakvv.thryvecasestudy.controller;

import io.restassured.filter.Filter;
import io.restassured.internal.RestAssuredResponseOptionsImpl;
import io.restassured.response.Response;

import static bayrakvv.thryvecasestudy.secrets.Secrets.PUTS_REQ_URL;
import static io.restassured.RestAssured.given;

public class PutsreqController {
    private static final Filter FORCE_JSON_RESPONSE_BODY = (reqSpec, respSpec, ctx) -> {
        Response response = ctx.next(reqSpec, respSpec);
        ((RestAssuredResponseOptionsImpl<?>) response).setContentType("application/json");
        return response;
    };

    public static Response getLastWebhookData() {
        return given()
            .filters(FORCE_JSON_RESPONSE_BODY)
            .get(PUTS_REQ_URL)
            .then()
            .statusCode(200)
            .extract()
            .response();
    }
}
