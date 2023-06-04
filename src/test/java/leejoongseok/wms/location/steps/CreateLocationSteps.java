package leejoongseok.wms.location.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.Scenario;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import leejoongseok.wms.location.feature.CreateLocation;
import org.springframework.http.HttpStatus;

public class CreateLocationSteps {
    public static class Request {
        private String locationBarcode = "A1-1-1";
        private StorageType storageType = StorageType.CELL;
        private UsagePurpose usagePurpose = UsagePurpose.STOW;

        public Request locationBarcode(final String locationBarcode) {
            this.locationBarcode = locationBarcode;
            return this;
        }

        public Request storageType(final StorageType storageType) {
            this.storageType = storageType;
            return this;
        }

        public Request usagePurpose(final UsagePurpose usagePurpose) {
            this.usagePurpose = usagePurpose;
            return this;
        }

        public Scenario request() {
            final CreateLocation.Request request = new CreateLocation.Request(
                    locationBarcode,
                    storageType,
                    usagePurpose
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/locations")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
            return new Scenario();
        }
    }
}
