package leejoongseok.wms.inbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.inbound.feature.CreateLPN;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class CreateLPNSteps {
    public static class Request {
        private LocalDateTime expirationAt = LocalDateTime.now().plusDays(1);
        private Long inboundId = 1L;
        private Long inboundItemId = 1L;
        private String lpnBarcode = "lpnBarcode";

        public Request expirationAt(final LocalDateTime expirationAt) {
            this.expirationAt = expirationAt;
            return this;
        }

        public Request inboundId(final Long inboundId) {
            this.inboundId = inboundId;
            return this;
        }

        public Request inboundItemId(final Long inboundItemId) {
            this.inboundItemId = inboundItemId;
            return this;
        }

        public Request lpnBarcode(final String lpnBarcode) {
            this.lpnBarcode = lpnBarcode;
            return this;
        }

        public Scenario request() {
            request(HttpStatus.CREATED.value());
            return new Scenario();
        }

        public Scenario request(final int statusCode) {
            final CreateLPN.Request request = new CreateLPN.Request(
                    lpnBarcode,
                    expirationAt
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/inbounds/{inboundId}/inbound-items/{inboundItemId}/lpns", inboundId, inboundItemId)
                    .then()
                    .log().all()
                    .statusCode(statusCode);
            return new Scenario();
        }
    }
}
