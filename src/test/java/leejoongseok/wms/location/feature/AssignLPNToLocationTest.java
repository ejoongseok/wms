package leejoongseok.wms.location.feature;

import leejoongseok.wms.ApiTest;
import leejoongseok.wms.Scenario;
import leejoongseok.wms.location.domain.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

class AssignLPNToLocationTest extends ApiTest {

    @Autowired
    private AssignLPNToLocation assignLPNToLocation;
    @Autowired
    private LocationRepository locationRepository;

    @Test
    @DisplayName("로케이션에 LPN을 할당한다.")
    @Transactional
    void assignLPNToLocation() {
        final String locationBarcode = "A1-1-1";
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request();

        final Location location = locationRepository.findByLocationBarcode(locationBarcode).get();
        assertThat(location.getLocationLPNList()).hasSize(1);
    }
}
