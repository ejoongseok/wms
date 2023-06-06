package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.CushioningMaterial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class CreateOutboundTest {

    @Test
    @DisplayName("출고를 생성한다.")
    void createOutbound() {
        final CreateOutbound createOutbound = new CreateOutbound();
        final Long orderId = 1L;
        final CushioningMaterial cushioningMaterial = null;
        final Integer cushioningMaterialQuantity = null;
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
