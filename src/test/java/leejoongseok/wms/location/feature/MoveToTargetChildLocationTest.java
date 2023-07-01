package leejoongseok.wms.location.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationRepository;
import leejoongseok.wms.location.domain.StorageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MoveToTargetChildLocationTest extends ApiTest {

    @Autowired
    private LocationRepository locationRepository;

    private static void createLocation(
            final String locationBarcode,
            final StorageType storageType) {
        new Scenario()
                .createLocation()
                .locationBarcode(locationBarcode)
                .storageType(storageType)
                .request();
    }

    @Test
    @DisplayName("대상 로케이션의 하위 로케이션으로 현재 로케이션을 이동한다.")
    void moveToTargetChildLocation() {
        final String cellLocationBarcode = "CELL-1";
        createLocation(cellLocationBarcode, StorageType.CELL);
        final String toteLocationBarcode = "TOTE-1";
        createLocation(toteLocationBarcode, StorageType.TOTE);
        final String toteLocationBarcode2 = "TOTE-2";
        createLocation(toteLocationBarcode2, StorageType.TOTE);

        new Scenario()
                .moveToTargetChildLocation()
                .currentLocationBarcode(cellLocationBarcode)
                .targetLocationBarcode(toteLocationBarcode)
                .request();

        final var cellLocation = getLocation(cellLocationBarcode);
        final var toteLocation = getLocation(toteLocationBarcode);
        assertThat(cellLocation.getParentLocation()).isEqualTo(toteLocation);
        assertThat(toteLocation.getChildLocations()).hasSize(1);

        new Scenario()
                .moveToTargetChildLocation()
                .currentLocationBarcode(cellLocationBarcode)
                .targetLocationBarcode(toteLocationBarcode2)
                .request();

        final Location reloadToteLocation = getLocation(toteLocationBarcode);
        assertThat(reloadToteLocation.getChildLocations()).isEmpty();

        final var toteLocation2 = getLocation(toteLocationBarcode2);
        final Location reloadCellLocation = getLocation(cellLocationBarcode);
        assertThat(reloadCellLocation.getParentLocation()).isEqualTo(toteLocation2);
        assertThat(toteLocation2.getChildLocations()).hasSize(1);

    }

    private Location getLocation(final String locationBarcode) {
        return locationRepository.testingFindByLocationBarcode(locationBarcode).get();
    }
}