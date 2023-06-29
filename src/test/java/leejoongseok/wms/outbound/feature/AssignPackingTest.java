package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.domain.PackagingMaterialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AssignPackingTest extends ApiTest {

    @Autowired
    private AssignPacking assignPacking;
    @Autowired
    private OutboundRepository outboundRepository;
    @Autowired
    private PackagingMaterialRepository packagingMaterialRepository;

    @Test
    @DisplayName("패킹 정보를 등록한다.")
    void assignPacking() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request(3)
                .createPackagingMaterial().request()
                .createOutbound().request();

        final String toteBarcode = "TOTE0001";
        new Scenario()
                .createLocation()
                .locationBarcode(toteBarcode)
                .storageType(StorageType.TOTE)
                .usagePurpose(UsagePurpose.MOVE)
                .request()
                .assignPickingTote().request()
                .allocatePicking().request()
                .manualToPick().pickedQuantity(2).request()
                .completePicking().request();

        final Long outboundId = 1L;
        final Long packagingMaterialId = 1L;
        final Integer realWeightInGrams = 30;
        final AssignPacking.Request request = new AssignPacking.Request(
                outboundId,
                packagingMaterialId,
                realWeightInGrams
        );

        assignPacking.request(request);

        // outboundRepository.findById(1L).get().isPackingInProgress();
    }
}