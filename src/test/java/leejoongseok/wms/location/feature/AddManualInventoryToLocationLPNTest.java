package leejoongseok.wms.location.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddManualInventoryToLocationLPNTest {

    private AddManualInventoryToLocationLPN sut;

    @BeforeEach
    void setUp() {
        sut = new AddManualInventoryToLocationLPN();
    }

    @Test
    @DisplayName("로케이션 LPN에 재고 수량을 직접 추가한다.")
    void addManualInventoryToLocationLPN() {
        final String lpnBarcode = "lpnBarcode";
        final String locationBarcode = "locationBarcode";
        final Integer inventoryQuantity = 10;
        final AddManualInventoryToLocationLPN.Request request = new AddManualInventoryToLocationLPN.Request(
                lpnBarcode,
                locationBarcode,
                inventoryQuantity
        );
        sut.request(request);
    }
}
