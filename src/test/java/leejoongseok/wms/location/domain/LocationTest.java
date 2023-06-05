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

        assertLocationLPN(location, lpnBarcode, lpn);
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

    private void assertLocationLPN(final Location location, final String lpnBarcode, final LPN lpn) {
        final LocationLPN locationLPN = location.getLocationLPN(lpnBarcode);
        assertThat(locationLPN).isNotNull();
        assertThat(locationLPN.getLpn()).isEqualTo(lpn);
        assertThat(location.getLocationLPNList()).hasSize(1);
    }
}