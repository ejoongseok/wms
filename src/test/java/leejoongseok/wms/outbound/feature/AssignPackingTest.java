package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.domain.PackagingMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AssignPackingTest {

    private AssignPacking assignPacking;
    private OutboundRepository outboundRepository;
    private PackagingMaterialRepository packagingMaterialRepository;

    @BeforeEach
    void setUp() {
        outboundRepository = null;
        packagingMaterialRepository = null;
        assignPacking = new AssignPacking(
                outboundRepository,
                packagingMaterialRepository);
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