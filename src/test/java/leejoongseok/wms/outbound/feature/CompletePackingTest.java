package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CompletePackingTest {

    private CompletePacking completePacking;

    @BeforeEach
    void setUp() {
        completePacking = new CompletePacking();
    }

    @Test
    @DisplayName("출고에대한 포장을 완료한다.")
    void completePacking() {
        final Long outboundId = 1L;
        completePacking.request(outboundId);
    }
}