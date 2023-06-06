package leejoongseok.wms.location.domain;

import leejoongseok.wms.inbound.domain.LPN;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocationTest {

    @Test
    @DisplayName("로케이션에 LPN을 등록한다.")
    void assignLPN() {
        final Location location = createLocation();
        final String lpnBarcode = "lpnBarcode";
        final LPN lpn = createLPN(lpnBarcode);

        location.assignLPN(lpn);

        final int locationLPNListSize = 1;
        final int expectedInventoryQuantity = 1;
        assertLocationLPN(location, lpnBarcode, lpn, locationLPNListSize, expectedInventoryQuantity);
    }

    @Test
    @DisplayName("로케이션에 같은 LPN을 두번 등록할 경우 LocationLPN은 하나만 생성되고 재고수량이 2가 된다.")
    void duplicate_assignLPN() {
        final Location location = createLocation();
        final String lpnBarcode = "lpnBarcode";
        final LPN lpn = createLPN(lpnBarcode);

        location.assignLPN(lpn);
        location.assignLPN(lpn);

        final int locationLPNListSize = 1;
        final int expectedInventoryQuantity = 2;
        assertLocationLPN(location, lpnBarcode, lpn, locationLPNListSize, expectedInventoryQuantity);
    }

    private Location createLocation() {
        return Instancio.of(Location.class)
                .ignore(Select.field(Location::getLocationLPNList))
                .create();
    }

    private LPN createLPN(final String lpnBarcode) {
        return Instancio.of(LPN.class)
                .supply(Select.field(LPN::getLpnBarcode), () -> lpnBarcode)
                .create();
    }

    private void assertLocationLPN(
            final Location location,
            final String lpnBarcode,
            final LPN lpn,
            final int locationLPNListSize,
            final int expectedInventoryQuantity) {
        final LocationLPN locationLPN = location.testingGetLocationLPN(lpnBarcode);
        assertThat(locationLPN).isNotNull();
        assertThat(locationLPN.getLpn()).isEqualTo(lpn);
        assertThat(location.getLocationLPNList()).hasSize(locationLPNListSize);
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(expectedInventoryQuantity);
    }

    @Test
    @DisplayName("직접 입력한 재고 수량을 로케이션 LPN에 추가한다.")
    void addManualInventoryToLocationLPN() {
        final Location location = createLocation();
        final String lpnBarcode = "lpnBarcode";
        final LPN lpn = createLPN(lpnBarcode);
        location.assignLPN(lpn);
        final int inventoryQuantity = 10;

        location.addManualInventoryToLocationLPN(lpn, inventoryQuantity);

        final int expectedInventoryQuantity = 11;
        assertLocationLPN(location, lpnBarcode, lpn, 1, expectedInventoryQuantity);

    }
}