package leejoongseok.wms.location.feature;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AddManualInventoryToLocationLPNTest {

    private AddManualInventoryToLocationLPN sut;
    private LocationRepository locationRepository;
    private LPNRepository lpnRepository;

    @BeforeEach
    void setUp() {
        locationRepository = Mockito.mock(LocationRepository.class);
        lpnRepository = Mockito.mock(LPNRepository.class);
        sut = new AddManualInventoryToLocationLPN(locationRepository, lpnRepository);
    }

    @Test
    @DisplayName("로케이션 LPN에 재고 수량을 직접 추가한다.")
    void addManualInventoryToLocationLPN() {
        final String lpnBarcode = "lpnBarcode";
        final String locationBarcode = "A-1-1";
        final LPN lpn = createLPN(lpnBarcode);
        final Location location = createLocation(locationBarcode);
        Mockito.when(lpnRepository.findByLPNBarcode(lpnBarcode)).thenReturn(java.util.Optional.of(lpn));
        Mockito.when(locationRepository.findByLocationBarcode(locationBarcode)).thenReturn(java.util.Optional.of(location));
        final Integer inventoryQuantity = 10;
        final AddManualInventoryToLocationLPN.Request request = new AddManualInventoryToLocationLPN.Request(
                lpnBarcode,
                locationBarcode,
                inventoryQuantity
        );

        sut.request(request);

    }

    private LPN createLPN(final String lpnBarcode) {
        return Instancio.of(LPN.class)
                .supply(Select.field(LPN::getLpnBarcode), () -> lpnBarcode)
                .create();
    }

    private Location createLocation(final String locationBarcode) {
        return Instancio.of(Location.class)
                .supply(Select.field(Location::getLocationBarcode), () -> locationBarcode)
                .create();
    }
}
