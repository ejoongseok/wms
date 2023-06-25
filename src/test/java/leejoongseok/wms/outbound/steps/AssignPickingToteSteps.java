package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.feature.AssignPickingTote;
import org.springframework.http.HttpStatus;

public class AssignPickingToteSteps {
    public static class Request {
        private Long outboundId = 1L;
        private String toteBarcode = "TOTE0001";

        public Request outboundId(final Long outboundId) {
            this.outboundId = outboundId;
            return this;
        }

        public Request toteBarcode(final String toteBarcode) {
            this.toteBarcode = toteBarcode;
            return this;
        }

        public Scenario request() {
            final AssignPickingTote.Request request = new AssignPickingTote.Request(
                    outboundId,
                    toteBarcode);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/outbounds/assign-picking-tote")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
