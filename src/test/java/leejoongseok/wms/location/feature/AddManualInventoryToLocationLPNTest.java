package leejoongseok.wms.location.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

class AddManualInventoryToLocationLPNTest extends ApiTest {

    @Autowired
    private AddManualInventoryToLocationLPN sut;
    @Autowired
    private LocationRepository locationRepository;


    @Test
    @Transactional
    @DisplayName("로케이션 LPN에 재고 수량을 직접 추가한다.")
    void addManualInventoryToLocationLPN() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request();

        final String lpnBarcode = "lpnBarcode";
        final String locationBarcode = "A1-1-1";
        final Integer inventoryQuantity = 10;
        final AddManualInventoryToLocationLPN.Request request = new AddManualInventoryToLocationLPN.Request(
                lpnBarcode,
                locationBarcode,
                inventoryQuantity
        );

        sut.request(request);

        final Location location = locationRepository.findByLocationBarcode(locationBarcode).get();
        final Integer expectedInventoryQuantity = location.getLocationLPNList().get(0).getInventoryQuantity();

        assertThat(expectedInventoryQuantity).isEqualTo(inventoryQuantity + 1);
    }
}
