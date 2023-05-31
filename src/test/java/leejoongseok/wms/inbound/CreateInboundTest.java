package leejoongseok.wms.inbound;

import leejoongseok.wms.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CreateInboundTest extends ApiTest {

    @Autowired
    private CreateInbound createInbound;
    @Autowired
    private InboundRepository inboundRepository;

    @Test
    @DisplayName("입고를 등록한다.")
    void createInbound() {
        final LocalDateTime orderRequestAt = LocalDateTime.now().minusDays(1);
        final LocalDateTime estimatedArrivalAt = LocalDateTime.now().plusDays(1);
        final BigDecimal totalAmount = BigDecimal.valueOf(2000);
        final CreateInbound.Request request = new CreateInbound.Request(
                orderRequestAt,
                estimatedArrivalAt,
                totalAmount
        );

        createInbound.request(request);

        final Inbound inbound = inboundRepository.inbounds.get(1L);
        assertThat(inbound).isNotNull();
    }
}
