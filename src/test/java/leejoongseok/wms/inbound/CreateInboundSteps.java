package leejoongseok.wms.inbound;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.Scenario;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CreateInboundSteps {
    public static class Request {
        private LocalDateTime orderRequestAt = LocalDateTime.now().minusDays(1);
        private LocalDateTime estimatedArrivalAt = LocalDateTime.now().plusDays(1);
        private BigDecimal totalAmount = BigDecimal.valueOf(1000);
        List<CreateInbound.Request.ItemRequest> itemRequests =
                List.of(
                        new CreateInbound.Request.ItemRequest(
                                1L,
                                2,
                                BigDecimal.valueOf(500),
                                "description")
                );

        public Request orderRequestAt(final LocalDateTime orderRequestAt) {
            this.orderRequestAt = orderRequestAt;
            return this;
        }

        public Request estimatedArrivalAt(final LocalDateTime estimatedArrivalAt) {
            this.estimatedArrivalAt = estimatedArrivalAt;
            return this;
        }

        public Request totalAmount(final BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Request itemRequests(final List<CreateInbound.Request.ItemRequest> itemRequests) {
            this.itemRequests = itemRequests;
            return this;
        }

        public Scenario request() {
            final CreateInbound.Request request = new CreateInbound.Request(
                    orderRequestAt,
                    estimatedArrivalAt,
                    totalAmount,
                    itemRequests
            );
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/inbounds")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
            return new Scenario();
        }
    }
}
