package leejoongseok.wms.inbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.inbound.feature.RejectInbound;
import org.springframework.http.HttpStatus;

public class RejectInboundSteps {
    public static class Request {
        private Long inboundId = 1L;
        private String rejectionReasons = "거부 사유";

        public Request inboundId(final Long inboundId) {
            this.inboundId = inboundId;
            return this;
        }

        public Request rejectionReasons(final String rejectionReasons) {
            this.rejectionReasons = rejectionReasons;
            return this;
        }

        public Scenario request() {
            final RejectInbound.Request request = new RejectInbound.Request(rejectionReasons);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when()
                    .body(request)
                    .post("/inbounds/{inboundId}/reject", inboundId)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
