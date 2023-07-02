package leejoongseok.wms.location.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.feature.AdjustInventory;
import org.springframework.http.HttpStatus;

public class AdjustInventorySteps {
    public static class Request {
        private String locationBarcode = "A1-1-1";
        private String lpnBarcode = "lpnBarcode";
        private Integer quantity = 5;
        private String reason = "reason";

        public Request locationBarcode(final String locationBarcode) {
            this.locationBarcode = locationBarcode;
            return this;
        }

        public Request lpnBarcode(final String lpnBarcode) {
            this.lpnBarcode = lpnBarcode;
            return this;
        }

        public Request quantity(final Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Request reason(final String reason) {
            this.reason = reason;
            return this;
        }

        public Scenario request() {
            final AdjustInventory.Request request = new AdjustInventory.Request(
                    locationBarcode,
                    lpnBarcode,
                    quantity,
                    reason
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/locations/adjust-inventory")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }

        public Scenario request(final Integer requestCount) {
            for (int i = 0; i < requestCount; i++) {
                request();
            }
            return new Scenario();
        }
    }
}
