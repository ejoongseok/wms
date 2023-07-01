package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PassInspectionTest {

    private PassInspection passInspection;
    private OutboundRepository outboundRepository;

    @BeforeEach
    void setUp() {
        outboundRepository = null;
        passInspection = new PassInspection(outboundRepository);
    }

    @Test
    @DisplayName("집품 완료한 출고건의 검수를 통과한다.")
    void passInspection() {
        final Long outboundId = 1L;
        passInspection.request(outboundId);
    }
}