package leejoongseok.wms.location.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.feature.TransferInventory;
import org.springframework.http.HttpStatus;

public class TransferInventorySteps {
    public static class Request {
        private String fromLocationBarcode = "A1-1-1";
        private String toLocationBarcode = "toLocationBarcode";
        private Long targetLPNId = 1L;
        private Integer transferQuantity = 5;

        public Request fromLocationBarcode(final String fromLocationBarcode) {
            this.fromLocationBarcode = fromLocationBarcode;
            return this;
        }

        public Request toLocationBarcode(final String toLocationBarcode) {
            this.toLocationBarcode = toLocationBarcode;
            return this;
        }

        public Request targetLPNId(final Long targetLPNId) {
            this.targetLPNId = targetLPNId;
            return this;
        }

        public Request transferQuantity(final Integer transferQuantity) {
            this.transferQuantity = transferQuantity;
            return this;
        }

        public Scenario request() {
            final TransferInventory.Request request = new TransferInventory.Request(
                    fromLocationBarcode,
                    toLocationBarcode,
                    targetLPNId,
                    transferQuantity
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/locations/location-lpns/transfer")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
