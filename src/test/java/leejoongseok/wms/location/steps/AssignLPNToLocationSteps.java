package leejoongseok.wms.location.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.Scenario;
import leejoongseok.wms.location.feature.AssignLPNToLocation;
import org.springframework.http.HttpStatus;

public class AssignLPNToLocationSteps {
    public static class Request {
        private String locationBarcode = "A1-1-1";
        private String lpnBarcode = "lpnBarcode";

        public Request locationBarcode(final String locationBarcode) {
            this.locationBarcode = locationBarcode;
            return this;
        }

        public Request lpnBarcode(final String lpnBarcode) {
            this.lpnBarcode = lpnBarcode;
            return this;
        }

        public Scenario request() {
            final AssignLPNToLocation.Request request = new AssignLPNToLocation.Request(
                    lpnBarcode,
                    locationBarcode);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/locations/assign-lpn")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
