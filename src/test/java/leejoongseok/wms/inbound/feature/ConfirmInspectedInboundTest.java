package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.domain.InboundStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ConfirmInspectedInboundTest extends ApiTest {

    @Autowired
    private InboundRepository inboundRepository;

    @Test
    @DisplayName("입고를 확정한다.")
    void confirmInspectedInbound() {
        Scenario
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
        ;

        assertThat(inboundRepository.findById(1L).get().getStatus()).isEqualTo(InboundStatus.CONFIRM_INSPECTED);
    }
}
