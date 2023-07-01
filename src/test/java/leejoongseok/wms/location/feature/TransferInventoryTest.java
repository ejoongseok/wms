package leejoongseok.wms.location.feature;

import leejoongseok.wms.location.domain.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransferInventoryTest {

    private TransferInventory transferInventory;
    private LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        locationRepository = null;
        transferInventory = new TransferInventory(locationRepository);
    }

    @Test
    @DisplayName("A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능")
    void transferInventory() {
        final String fromLocationBarcode = "fromLocationBarcode";
        final String toLocationBarcode = "toLocationBarcode";
        final Long targetLPNId = 1L;
        final Integer transferQuantity = 5;
        final TransferInventory.Request request = new TransferInventory.Request(
                fromLocationBarcode,
                toLocationBarcode,
                targetLPNId,
                transferQuantity
        );
        transferInventory.request(request);
    }
}