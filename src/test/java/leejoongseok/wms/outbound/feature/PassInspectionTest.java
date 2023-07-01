package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PassInspectionTest {

    private PassInspection passInspection;

    @BeforeEach
    void setUp() {
        passInspection = new PassInspection();
    }

    @Test
    @DisplayName("검수 통과")
    void passInspection() {
        final Long outboundId = 1L;
        passInspection.request(outboundId);
    }
}