package leejoongseok.wms.inbound;

import leejoongseok.wms.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateInboundTest extends ApiTest {

    @Autowired
    private CreateInbound createInbound;
    @Autowired
    private InboundRepository inboundRepository;

    @Test
    @DisplayName("입고를 등록한다.")
    void createInbound() {
        final long itemId = 1L;
        final CreateInbound.Request.ItemRequest itemRequest = new CreateInbound.Request.ItemRequest(itemId, 1, BigDecimal.valueOf(1000), "파손 주의 상품");


        final LocalDateTime orderRequestAt = LocalDateTime.now().minusDays(1);
        final LocalDateTime estimatedArrivalAt = LocalDateTime.now().plusDays(1);
        final BigDecimal totalAmount = BigDecimal.valueOf(2000);
        final CreateInbound.Request request = new CreateInbound.Request(
                orderRequestAt,
                estimatedArrivalAt,
                totalAmount,
                List.of(itemRequest)
        );

        createInbound.request(request);

        final Inbound inbound = inboundRepository.findById(1L).get();
        assertThat(inbound).isNotNull();
    }
}
