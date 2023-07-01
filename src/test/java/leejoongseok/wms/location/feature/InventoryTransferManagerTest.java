package leejoongseok.wms.location.feature;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.location.domain.Location;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryTransferManagerTest {

    @Test
    @DisplayName("A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능")
    void transfer() {
        // given
        final Location fromLocation = createLocation();
        final long lpnId = 1L;
        final String lpnBarcode = "LPN-1";
        final LPN lpn = createLPN(lpnId, lpnBarcode);
        fromLocation.assignLPN(lpn);
        final int inventoryQuantity = 10;
        fromLocation.addManualInventoryToLocationLPN(lpn, inventoryQuantity);
        final Location toLocation = createLocation();
        final Integer transferQuantity = 5;

        // when
        InventoryTransferManager.transfer(
                fromLocation,
                toLocation,
                lpnId,
                transferQuantity);

        // then
        assertThat(fromLocation.testingGetLocationLPN(lpnBarcode).getInventoryQuantity()).isEqualTo(6);
        assertThat(toLocation.testingGetLocationLPN(lpnBarcode).getInventoryQuantity()).isEqualTo(5);
    }

    private Location createLocation() {
        return Instancio.of(Location.class)
                .ignore(Select.field(Location::getLocationLPNList))
                .create();
    }

    private LPN createLPN(
            final long lpnId,
            final String lpnBarcode) {
        return Instancio.of(LPN.class)
                .supply(Select.field(LPN::getId), () -> lpnId)
                .supply(Select.field(LPN::getLpnBarcode), () -> lpnBarcode)
                .create();
    }
}