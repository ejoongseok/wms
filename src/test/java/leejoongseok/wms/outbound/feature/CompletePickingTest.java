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

class CompletePickingTest extends ApiTest {

    @Autowired
    private CompletePicking completePicking;
    @Autowired
    private OutboundRepository outboundRepository;

    @Test
    @DisplayName("출고해야할 상품의 집품이 모두 완료되면 집품완료를 할 수 있다.")
    void completePicking() {
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
                .manualToPick().pickedQuantity(2).request();

        final Long outboundId = 1L;
        final CompletePicking.Request request = new CompletePicking.Request(
                outboundId
        );

        completePicking.request(request);

        assertThat(outboundRepository.findById(1L).get().isCompletedPicking()).isTrue();
    }
}