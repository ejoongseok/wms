package leejoongseok.wms.location.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AssignLPNToLocationTest {

    private AssignLPNToLocation assignLPNToLocation;

    @BeforeEach
    void setUp() {
        assignLPNToLocation = new AssignLPNToLocation();
    }

    @Test
    @DisplayName("로케이션에 LPN을 할당한다.")
    void assignLPNToLocation() {
        final String lpnBarcode = "lpnBarcode";
        final String locationBarcode = "A1-1-1";
        final AssignLPNToLocation.Request request = new AssignLPNToLocation.Request(
                lpnBarcode,
                locationBarcode);

        assignLPNToLocation.request(request);
    }
}
