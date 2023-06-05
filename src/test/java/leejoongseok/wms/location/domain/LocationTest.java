package leejoongseok.wms.location.domain;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocationTest {

    @Test
    @DisplayName("로케이션에 LPN을 등록한다.")
    void assignLPN() {
        final Location location = Instancio.create(Location.class);
        final String lpnBarcode = "lpnBarcode";

        location.assignLPN(lpnBarcode);

        final LocationLPN locationLPN = location.getLocationLPN(lpnBarcode);
        assertThat(locationLPN).isNotNull();
    }
}