package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResetOutboundTest {

    private ResetOutbound resetOutbound;

    @BeforeEach
    void setUp() {
        resetOutbound = new ResetOutbound();
    }

    @Test
    @DisplayName("출고를 초기화한다.")
    void resetOutbound() {
        final Long outboundId = 1L;
        resetOutbound.request(outboundId);
    }
}