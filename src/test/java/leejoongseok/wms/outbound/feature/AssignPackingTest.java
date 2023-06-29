package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AssignPackingTest extends ApiTest {

    @Autowired
    private OutboundRepository outboundRepository;

    @Test
    @DisplayName("출고를 포장한뒤 포장 정보를 입력한다.")
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
                .completePicking().request()
                .assignPacking().request();

        assertThat(outboundRepository.findById(1L).get().isPackingInProgress()).isTrue();
    }
}