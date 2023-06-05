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
        final Location location = Instancio.create(Location.class);
        final String lpnBarcode = "lpnBarcode";
        final LPN lpn = Instancio.of(LPN.class)
                .supply(Select.field(LPN::getLpnBarcode), () -> lpnBarcode)
                .create();

        location.assignLPN(lpn);

        final LocationLPN locationLPN = location.getLocationLPN(lpnBarcode);
        assertThat(locationLPN).isNotNull();
    }
}