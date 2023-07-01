package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.feature.FailInspection;
import org.springframework.http.HttpStatus;

public class FailInspectionSteps {
    public static class Request {
        private Long outboundId = 1L;
        private String stoppedReason = "품질불량";

        public Request outboundId(final Long outboundId) {
            this.outboundId = outboundId;
            return this;
        }

        public Request stoppedReason(final String stoppedReason) {
            this.stoppedReason = stoppedReason;
            return this;
        }

        public Scenario request() {
            final FailInspection.Request request = new FailInspection.Request(
                    stoppedReason);
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when()
                    .body(request)
                    .post("/outbounds/{outboundId}/fail-inspection", outboundId)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
