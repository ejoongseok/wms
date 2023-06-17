package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SplitToOutboundTest {

    private SplitToOutbound splitToOutbound;

    @BeforeEach
    void setUp() {
        splitToOutbound = new SplitToOutbound();
    }

    @Test
    @DisplayName("대기중인 출고건을 분할한다.")
    void splitToOutbound() {
        final SplitToOutbound.Request request = new SplitToOutbound.Request();
        splitToOutbound.request(request);
    }
}
