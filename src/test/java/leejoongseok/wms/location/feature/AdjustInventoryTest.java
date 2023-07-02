package leejoongseok.wms.location.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdjustInventoryTest {


    private AdjustInventory adjustInventory;

    @BeforeEach
    void setUp() {
        adjustInventory = new AdjustInventory();
    }

    @Test
    @DisplayName("재고를 조정한다.")
    void adjustInventory() {
        final String locationBarcode = "locationBarcode";
        final String lpnBarcode = "lpnBarcode";
        final Integer quantity = 5;
        final String reason = "reason";
        final AdjustInventory.Request request = new AdjustInventory.Request(
                locationBarcode,
                lpnBarcode,
                quantity,
                reason
        );
        adjustInventory.request(request);
    }
}