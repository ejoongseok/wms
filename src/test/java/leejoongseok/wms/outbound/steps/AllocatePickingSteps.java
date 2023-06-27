package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.feature.AllocatePicking;
import org.springframework.http.HttpStatus;

public class AllocatePickingSteps {
    public static class Request {
        private Long outboundId = 1L;

        public Request outboundId(final Long outboundId) {
            this.outboundId = outboundId;
            return this;
        }

        public Scenario request() {
            final AllocatePicking.Request request = new AllocatePicking.Request(
                    outboundId);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/outbounds/allocate-picking")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
