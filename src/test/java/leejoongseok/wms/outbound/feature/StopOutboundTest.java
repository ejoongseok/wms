package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class StopOutboundTest extends ApiTest {

    @Autowired
    private StopOutbound stopOutbound;
    @Autowired
    private OutboundRepository outboundRepository;

    @Test
    @DisplayName("어떠한 이유로 출고를 중지한다.")
    void stopOutbound() {
        final String stoppedReason = "오집품/상품불량/포장손상..";
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request(2)
                .createPackagingMaterial().request()
                .createOutbound().request()
                .stopOutbound()
                .stoppedReason(stoppedReason)
                .request();


        final Outbound outbound = outboundRepository.findById(1L).get();
        assertThat(outbound.isStopped()).isTrue();
        assertThat(outbound.getStoppedReason()).isEqualTo(stoppedReason);
    }
}