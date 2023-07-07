package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.domain.InboundStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RejectInboundTest extends ApiTest {
    @Autowired
    InboundRepository inboundRepository;
    @Autowired
    RejectInbound rejectInbound;

    @Test
    @DisplayName("입고를 거부한다.")
    void rejectInbound() {
        Scenario
                .createItem().request()
                .createInbound().request()
                .rejectInbound().request();

        final Inbound inbound = inboundRepository.findById(1L).get();

        assertThat(inbound.getStatus()).isEqualTo(InboundStatus.REJECTED);

    }
}
