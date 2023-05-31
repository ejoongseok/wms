package leejoongseok.wms.inbound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class CreateInboundTest {

    @Test
    @DisplayName("입고를 등록한다.")
    void createInbound() {
        final LocalDateTime orderRequestAt = LocalDateTime.now().minusDays(1);
        final LocalDateTime estimatedArrivalAt = LocalDateTime.now().plusDays(1);
        final BigDecimal totalAmount = BigDecimal.valueOf(2000);
        final CreateInbound createInbound = new CreateInbound();
        final CreateInbound.Request request = new CreateInbound.Request(
                orderRequestAt,
                estimatedArrivalAt,
                totalAmount
        );

        createInbound.request(request);
    }
}
