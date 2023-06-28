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

class ManualToPickTest extends ApiTest {

    @Autowired
    private PickingRepository pickingRepository;

    @Test
    @DisplayName("집품해야할 로케이션에 가서 LPN을 선택하고 토트에 담을 상품의 수량을 직접 입력합니다.")
    void manualToPick() {

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
                .manualToPick().request();

        final Picking picking = pickingRepository.findById(1L).get();
        assertThat(picking.getPickedQuantity()).isEqualTo(1);
        assertThat(picking.isInProgress()).isTrue();
    }
}