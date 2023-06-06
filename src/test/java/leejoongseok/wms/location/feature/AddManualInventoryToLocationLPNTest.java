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
                .assignLPNToLocation().request()
                .addManualInventoryToLocationLPN().request();

        final String locationBarcode = "A1-1-1";

        final Location location = locationRepository.findByLocationBarcode(locationBarcode).get();
        final Integer expectedInventoryQuantity = location.getLocationLPNList().get(0).getInventoryQuantity();
        // 검증하려는 재고가 2인 이유는 assignLPN에서 최초로 LocationLPN을 생성할때 재고가 1으로 생성되고 여기서 1을 더해 2가 된다.
        assertThat(expectedInventoryQuantity).isEqualTo(2);
    }
}
