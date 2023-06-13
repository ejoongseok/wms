package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.CushioningMaterial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class CreateOutboundTest extends ApiTest {

    @Autowired
    private CreateOutbound createOutbound;

    @Test
    @DisplayName("출고를 생성한다.")
    void createOutbound() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request()
                .createPackagingMaterial().request();

        final Long orderId = 1L;
        final CushioningMaterial cushioningMaterial = CushioningMaterial.NONE;
        final Integer cushioningMaterialQuantity = 0;
        final Boolean isPriorityDelivery = true;
        final LocalDate desiredDeliveryDate = LocalDate.now();

        final CreateOutbound.Request request = new CreateOutbound.Request(
                orderId,
                cushioningMaterial,
                cushioningMaterialQuantity,
                isPriorityDelivery,
                desiredDeliveryDate
        );

        createOutbound.request(request);

    }
}
