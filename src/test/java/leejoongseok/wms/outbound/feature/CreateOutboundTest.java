package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.CushioningMaterial;
import leejoongseok.wms.outbound.domain.LocationLPNFilterForOutbound;
import leejoongseok.wms.outbound.domain.LocationLPNValidatorForOutbound;
import leejoongseok.wms.outbound.port.LoadOrderPort;
import leejoongseok.wms.packaging.domain.PackagingMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class CreateOutboundTest {

    private CreateOutbound createOutbound;
    private LoadOrderPort loadOrderPort;
    private LocationLPNRepository locationLPNRepository;
    private LocationLPNFilterForOutbound locationLPNFilterForOutbound;
    private LocationLPNValidatorForOutbound locationLPNValidatorForOutbound;
    private PackagingMaterialRepository packagingMaterialRepository;

    @BeforeEach
    void setUp() {
        loadOrderPort = new LoadOrderPort();
        locationLPNRepository = null;
        locationLPNFilterForOutbound = null;
        locationLPNValidatorForOutbound = null;
        packagingMaterialRepository = null;
        createOutbound = new CreateOutbound(
                loadOrderPort,
                locationLPNRepository,
                locationLPNFilterForOutbound,
                locationLPNValidatorForOutbound,
                packagingMaterialRepository);
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
