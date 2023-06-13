package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.CushioningMaterial;
import leejoongseok.wms.outbound.feature.CreateOutbound;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class CreateOutboundSteps {
    public static class Request {
        private Long orderId = 1L;
        private CushioningMaterial cushioningMaterial = CushioningMaterial.NONE;
        private Integer cushioningMaterialQuantity = 0;
        private Boolean isPriorityDelivery = true;
        private LocalDate desiredDeliveryDate = LocalDate.now();

        public Request orderId(final Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Request cushioningMaterial(final CushioningMaterial cushioningMaterial) {
            this.cushioningMaterial = cushioningMaterial;
            return this;
        }

        public Request cushioningMaterialQuantity(final Integer cushioningMaterialQuantity) {
            this.cushioningMaterialQuantity = cushioningMaterialQuantity;
            return this;
        }

        public Request isPriorityDelivery(final Boolean isPriorityDelivery) {
            this.isPriorityDelivery = isPriorityDelivery;
            return this;
        }

        public Request desiredDeliveryDate(final LocalDate desiredDeliveryDate) {
            this.desiredDeliveryDate = desiredDeliveryDate;
            return this;
        }

        public Scenario request() {
            final CreateOutbound.Request request = new CreateOutbound.Request(
                    orderId,
                    cushioningMaterial,
                    cushioningMaterialQuantity,
                    isPriorityDelivery,
                    desiredDeliveryDate
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/outbounds")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
            return new Scenario();
        }
    }
}
