package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.feature.ScanToPick;
import org.springframework.http.HttpStatus;

public class ScanToPickSteps {
    public static class Request {
        private Long pickingId = 1L;
        private String locationBarcode = "A1-1-1";
        private String lpnBarcode = "lpnBarcode";
        private final Long outboundId = 1L;

        public Request pickingId(final Long pickingId) {
            this.pickingId = pickingId;
            return this;
        }

        public Request locationBarcode(final String locationBarcode) {
            this.locationBarcode = locationBarcode;
            return this;
        }

        public Request lpnBarcode(final String lpnBarcode) {
            this.lpnBarcode = lpnBarcode;
            return this;
        }

        public Scenario request() {
            final ScanToPick.Request request = new ScanToPick.Request(
                    pickingId,
                    locationBarcode,
                    lpnBarcode
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/outbounds/pickings/scan-to-pick")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
