package leejoongseok.wms.location.feature;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
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
    private LPNRepository lpnRepository;

    @BeforeEach
    void setUp() {
        locationRepository = Mockito.mock(LocationRepository.class);
        lpnRepository = Mockito.mock(LPNRepository.class);
        assignLPNToLocation = new AssignLPNToLocation(locationRepository, lpnRepository);
    }

    @Test
    @DisplayName("로케이션에 LPN을 할당한다.")
    void assignLPNToLocation() {
        final String lpnBarcode = "lpnBarcode";
        final String locationBarcode = "A1-1-1";
        Mockito.when(lpnRepository.findByLPNBarcode(lpnBarcode))
                .thenReturn(Optional.of(Instancio.create(LPN.class)));
        Mockito.when(locationRepository.findByLocationBarcode(locationBarcode))
                .thenReturn(Optional.of(Instancio.create(Location.class)));
        final AssignLPNToLocation.Request request = new AssignLPNToLocation.Request(
                lpnBarcode,
                locationBarcode);

        assignLPNToLocation.request(request);
    }
}
