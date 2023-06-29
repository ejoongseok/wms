package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CompletePackingTest {

    private CompletePacking completePacking;
    private OutboundRepository outboundRepository;

    @BeforeEach
    void setUp() {
        outboundRepository = null;
        completePacking = new CompletePacking(outboundRepository);
    }

    @Test
    @DisplayName("출고에대한 포장을 완료한다.")
    void completePacking() {
        final Long outboundId = 1L;
        completePacking.request(outboundId);
    }
}