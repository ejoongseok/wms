package leejoongseok.wms.inbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import org.springframework.http.HttpStatus;

public class ConfirmInspectedInboundSteps {
    public static class Request {
        private Long inboundId = 1L;

        public Request inboundId(final Long inboundId) {
            this.inboundId = inboundId;
            return this;
        }

        public Scenario request() {

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/inbounds/{inboundId}/confirm-inspected", inboundId)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
            return new Scenario();
        }
    }
}
