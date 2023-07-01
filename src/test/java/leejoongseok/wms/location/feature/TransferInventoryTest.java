package leejoongseok.wms.location.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransferInventoryTest {

    private TransferInventory transferInventory;

    @BeforeEach
    void setUp() {
        transferInventory = new TransferInventory();
    }

    @Test
    @DisplayName("A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능")
    void transferInventory() {
        final Long fromLocationId = 1L;
        final Long toLocationId = 2L;
        final Long transferQuantity = 5L;
        final TransferInventory.Request request = new TransferInventory.Request(
                fromLocationId,
                toLocationId,
                transferQuantity
        );
        transferInventory.request(request);
    }
}