package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.feature.ManualToPick;
import org.springframework.http.HttpStatus;

public class ManualToPickSteps {
    public static class Request {
        private Long pickingId = 1L;
        private Long locationLPNId = 1L;
        private Integer pickedQuantity = 1;

        public Request pickingId(final Long pickingId) {
            this.pickingId = pickingId;
            return this;
        }

        public Request locationLPNId(final Long locationLPNId) {
            this.locationLPNId = locationLPNId;
            return this;
        }

        public Request pickedQuantity(final Integer pickedQuantity) {
            this.pickedQuantity = pickedQuantity;
            return this;
        }

        public Scenario request() {
            final ManualToPick.Request request = new ManualToPick.Request(
                    pickingId,
                    locationLPNId,
                    pickedQuantity
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/outbounds/pickings/manual-to-pick")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
