package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StopOutboundTest {

    private StopOutbound stopOutbound;
    private OutboundRepository outboundRepository;

    @BeforeEach
    void setUp() {
        outboundRepository = null;
        stopOutbound = new StopOutbound(outboundRepository);
    }

    @Test
    @DisplayName("어떠한 이유로 출고를 중지한다.")
    void stopOutbound() {
        final Long outboundId = 1L;
        final String stoppedReason = "오집품/상품불량/포장손상..";
        final StopOutbound.Request request = new StopOutbound.Request(
                stoppedReason
        );
        stopOutbound.request(outboundId, request);
    }
}