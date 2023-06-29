package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AssignPackingTest {

    private AssignPacking assignPacking;

    @BeforeEach
    void setUp() {
        assignPacking = new AssignPacking();
    }

    @Test
    @DisplayName("패킹 정보를 등록한다.")
    void assignPacking() {
        final Long outboundId = 1L;
        final Long packagingMaterialId = 1L;
        final Integer realWeightInGrams = 1000;
        final AssignPacking.Request request = new AssignPacking.Request(
                outboundId,
                packagingMaterialId,
                realWeightInGrams
        );

        assignPacking.request(request);

        // outboundRepository.findById(1L).get().isPackingInProgress();
    }
}