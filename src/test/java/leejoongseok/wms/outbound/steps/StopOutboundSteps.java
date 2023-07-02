package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.feature.StopOutbound;
import org.springframework.http.HttpStatus;

public class StopOutboundSteps {
    public static class Request {
        private Long outboundId = 1L;
        private String stoppedReason = "오집품";

        public Request outboundId(final Long outboundId) {
            this.outboundId = outboundId;
            return this;
        }

        public Request stoppedReason(final String stoppedReason) {
            this.stoppedReason = stoppedReason;
            return this;
        }

        public Scenario request() {

            final StopOutbound.Request request = new StopOutbound.Request(
                    stoppedReason
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/outbounds/{outboundId}/stop", outboundId)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
