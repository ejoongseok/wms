package leejoongseok.wms.location.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.feature.MoveToTargetChildLocation;
import org.springframework.http.HttpStatus;

public class MoveToTargetChildLocationSteps {
    public static class Request {
        private String currentLocationBarcode = "A1-1-1";
        private String targetLocationBarcode = "A1-1-1";

        public Request currentLocationBarcode(final String currentLocationBarcode) {
            this.currentLocationBarcode = currentLocationBarcode;
            return this;
        }

        public Request targetLocationBarcode(final String targetLocationBarcode) {
            this.targetLocationBarcode = targetLocationBarcode;
            return this;
        }

        public Scenario request() {
            final MoveToTargetChildLocation.Request request = new MoveToTargetChildLocation.Request(
                    currentLocationBarcode,
                    targetLocationBarcode);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/locations/move-to-target-child-location")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
