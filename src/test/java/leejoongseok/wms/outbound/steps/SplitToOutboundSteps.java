package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.feature.SplitToOutbound;
import org.springframework.http.HttpStatus;

import java.util.List;

public class SplitToOutboundSteps {
    public static class Request {
        private Integer cushioningMaterialQuantity = 1;
        private Long outBoundIdToSplit = 1L;
        private List<SplitToOutbound.Request.Item> itemsToSplit = List.of(
                new SplitToOutbound.Request.Item(1L, 2)
        );

        public Request cushioningMaterialQuantity(final Integer cushioningMaterialQuantity) {
            this.cushioningMaterialQuantity = cushioningMaterialQuantity;
            return this;
        }

        public Request outBoundIdToSplit(final Long outBoundIdToSplit) {
            this.outBoundIdToSplit = outBoundIdToSplit;
            return this;
        }

        public Request itemsToSplit(final List<SplitToOutbound.Request.Item> itemsToSplit) {
            this.itemsToSplit = itemsToSplit;
            return this;
        }

        public Scenario request() {
            final SplitToOutbound.Request request = new SplitToOutbound.Request(
                    cushioningMaterialQuantity,
                    outBoundIdToSplit,
                    itemsToSplit
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/outbounds/split")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
