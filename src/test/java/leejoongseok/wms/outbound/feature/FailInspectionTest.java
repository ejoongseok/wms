package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FailInspectionTest {

    private FailInspection failInspection;
    private OutboundRepository outboundRepository;

    @BeforeEach
    void setUp() {
        outboundRepository = null;
        failInspection = new FailInspection(outboundRepository);
    }

    @Test
    @DisplayName("출고 검수 불합격")
    void failInspection() {
        final Long outboundId = 1L;
        final String stoppedReason = "품질 불량";
        final FailInspection.Request request = new FailInspection.Request(outboundId, stoppedReason);

        failInspection.request(request);
    }
}