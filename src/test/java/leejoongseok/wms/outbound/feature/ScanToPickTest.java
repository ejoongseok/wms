package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.PickingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScanToPickTest {

    private ScanToPick scanToPick;
    private PickingRepository pickingRepository;
    private LocationLPNRepository locationLPNRepository;

    @BeforeEach
    void setUp() {
        pickingRepository = null;
        locationLPNRepository = null;
        scanToPick = new ScanToPick(
                pickingRepository,
                locationLPNRepository);
    }

    @Test
    @DisplayName("집품정보를 확인한 뒤 집품할 장소에가서 LocationBarcode와 상품의 LPNBarcode를 스캔해서 집품한다.")
    void scanToPick() {
        final Long pickingId = 1L;
        final String locationBarcode = "A-1-1";
        final String lpnBarcode = "LPN-1";
        final ScanToPick.Request request = new ScanToPick.Request(
                pickingId,
                locationBarcode,
                lpnBarcode
        );

        scanToPick.request(request);

        // Picking picking = pickingRepository.findById(pickingId).get();
        // picking.pickedQuantity.ast(1);
    }
}