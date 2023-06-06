package leejoongseok.wms.location.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.feature.AddManualInventoryToLocationLPN;
import org.springframework.http.HttpStatus;

public class AddManualInventoryToLocationLPNSteps {
    public static class Request {
        private String locationBarcode = "A1-1-1";
        private String lpnBarcode = "lpnBarcode";
        private Integer inventoryQuantity = 1;

        public Request locationBarcode(final String locationBarcode) {
            this.locationBarcode = locationBarcode;
            return this;
        }

        public Request lpnBarcode(final String lpnBarcode) {
            this.lpnBarcode = lpnBarcode;
            return this;
        }

        public Request inventoryQuantity(final Integer inventoryQuantity) {
            this.inventoryQuantity = inventoryQuantity;
            return this;
        }

        public Scenario request() {
            final AddManualInventoryToLocationLPN.Request request = new AddManualInventoryToLocationLPN.Request(
                    lpnBarcode,
                    locationBarcode,
                    inventoryQuantity
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/locations/location-lpns/add-manual-inventory")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
