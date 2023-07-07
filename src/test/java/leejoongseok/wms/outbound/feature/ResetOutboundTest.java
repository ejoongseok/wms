package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ResetOutboundTest extends ApiTest {

    @Autowired
    private OutboundRepository outboundRepository;

    @Test
    @DisplayName("출고를 초기화한다.")
    void resetOutbound() {
        Scenario
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request(2)
                .createPackagingMaterial().request()
                .createOutbound().request()
                .stopOutbound().request()
                .resetOutbound().request();

        final var outbound = outboundRepository.findById(1L).get();
        assertThat(outbound.isReadyStatus()).isTrue();
    }
}