package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.feature.AssignPacking;
import org.springframework.http.HttpStatus;

public class AssignPackingSteps {
    public static class Request {
        private Long outboundId = 1L;
        private Long packagingMaterialId = 1L;
        private Integer realWeightInGrams = 30;

        public Request outboundId(final Long outboundId) {
            this.outboundId = outboundId;
            return this;
        }

        public Request packagingMaterialId(final Long packagingMaterialId) {
            this.packagingMaterialId = packagingMaterialId;
            return this;
        }

        public Request realWeightInGrams(final Integer realWeightInGrams) {
            this.realWeightInGrams = realWeightInGrams;
            return this;
        }

        public Scenario request() {
            final AssignPacking.Request request = new AssignPacking.Request(
                    packagingMaterialId,
                    realWeightInGrams
            );
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when()
                    .body(request)
                    .post("/outbounds/{outboundId}/packings", outboundId)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
