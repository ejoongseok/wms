package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import leejoongseok.wms.outbound.domain.Picking;
import leejoongseok.wms.outbound.domain.PickingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ScanToPickTest extends ApiTest {

    @Autowired
    private ScanToPick scanToPick;
    @Autowired
    private PickingRepository pickingRepository;

    @Test
    @DisplayName("집품정보를 확인한 뒤 집품할 장소에가서 LocationBarcode와 상품의 LPNBarcode를 스캔해서 집품한다.")
    void scanToPick() {
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
                .scanToPick().request();


        final Picking picking = pickingRepository.findById(1L).get();
        assertThat(picking.getPickedQuantity()).isEqualTo(1);
    }
}