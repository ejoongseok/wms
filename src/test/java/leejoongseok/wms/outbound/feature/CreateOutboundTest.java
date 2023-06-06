package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.CushioningMaterial;
import leejoongseok.wms.outbound.port.LoadOrderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class CreateOutboundTest {

    private CreateOutbound createOutbound;
    private LoadOrderPort loadOrderPort;

    @BeforeEach
    void setUp() {
        loadOrderPort = null;
        createOutbound = new CreateOutbound(loadOrderPort);
    }

    @Test
    @DisplayName("출고를 생성한다.")
    void createOutbound() {
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
