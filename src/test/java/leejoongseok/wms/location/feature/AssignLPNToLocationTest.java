package leejoongseok.wms.location.feature;

import leejoongseok.wms.location.domain.Location;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class AssignLPNToLocationTest {

    private AssignLPNToLocation assignLPNToLocation;
    private LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        locationRepository = Mockito.mock(LocationRepository.class);
        assignLPNToLocation = new AssignLPNToLocation(locationRepository);
    }

    @Test
    @DisplayName("로케이션에 LPN을 할당한다.")
    void assignLPNToLocation() {
        Mockito.when(locationRepository.findByLocationBarcode("A1-1-1"))
                .thenReturn(Optional.of(Instancio.create(Location.class)));
        final String lpnBarcode = "lpnBarcode";
        final String locationBarcode = "A1-1-1";
        final AssignLPNToLocation.Request request = new AssignLPNToLocation.Request(
                lpnBarcode,
                locationBarcode);

        assignLPNToLocation.request(request);
    }
}
