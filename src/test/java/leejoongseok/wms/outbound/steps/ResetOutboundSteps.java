package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import org.springframework.http.HttpStatus;

public class ResetOutboundSteps {
    public static class Request {
        private Long outboundId = 1L;

        public Request outboundId(final Long outboundId) {
            this.outboundId = outboundId;
            return this;
        }

        public Scenario request() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/outbounds/{outboundId}/reset", outboundId)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
