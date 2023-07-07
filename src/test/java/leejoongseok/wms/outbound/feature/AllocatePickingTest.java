package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

public class AllocatePickingTest extends ApiTest {


    @Autowired
    private AllocatePicking allocatePicking;

    @Autowired
    private OutboundRepository outboundRepository;
    @Autowired
    LocationLPNRepository locationLPNRepository;

    @Test
    @DisplayName("출고 상품에 대한 집품 목록을 할당한다.")
    @Transactional
    void allocatePicking() {
        Scenario
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request(3)
                .createPackagingMaterial().request()
                .createOutbound().request();

        final String toteBarcode = "TOTE0001";
        Scenario
                .createLocation()
                .locationBarcode(toteBarcode)
                .storageType(StorageType.TOTE)
                .usagePurpose(UsagePurpose.MOVE)
                .request()
                .assignPickingTote().request()
                .allocatePicking().request();

        final var outbound = outboundRepository.findById(1L).get();
        assertThat(outbound.isPickingInProgress()).isTrue();
        assertThat(outbound.getOutboundItems().get(0).getPickings()).isNotEmpty();
        assertThat(locationLPNRepository.findById(1L).get().getInventoryQuantity()).isEqualTo(1);
    }
}
