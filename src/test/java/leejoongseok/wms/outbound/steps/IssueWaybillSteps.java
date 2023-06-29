package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import org.springframework.http.HttpStatus;

public class IssueWaybillSteps {
    public static class Request {
        private final Long outboundId = 1L;

        public Scenario request() {

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/outbounds/{outboundId}/issue-waybill", outboundId)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
