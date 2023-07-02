package leejoongseok.wms.location.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.AdjustInventoryHistoryRepository;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AdjustInventoryTest extends ApiTest {


    @Autowired
    private LocationLPNRepository locationLPNRepository;
    @Autowired
    private AdjustInventoryHistoryRepository adjustInventoryHistoryRepository;

    @Test
    @DisplayName("재고를 조정한다.")
    void adjustInventory() {
        final String locationBarcode = "A1-1-1";
        final String lpnBarcode = "LPNBarcode";

        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().lpnBarcode(lpnBarcode).request()
                .createLocation().locationBarcode(locationBarcode).request()
                .assignLPNToLocation().locationBarcode(locationBarcode).lpnBarcode(lpnBarcode).request();

        final LocationLPN locationLPN = getLocationLPN(locationBarcode, lpnBarcode);
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(1);

        final int quantity = 5;
        new Scenario().adjustInventory()
                .locationBarcode(locationBarcode)
                .lpnBarcode(lpnBarcode)
                .quantity(quantity)
                .request();

        final LocationLPN afterLocationLPN = getLocationLPN(locationBarcode, lpnBarcode);
        assertThat(afterLocationLPN.getInventoryQuantity()).isEqualTo(quantity);
        assertThat(adjustInventoryHistoryRepository.findAll()).hasSize(1);
    }

    private LocationLPN getLocationLPN(
            final String locationBarcode,
            final String lpnBarcode) {
        return locationLPNRepository.findByLocationBarcodeAndLPNBarcode(locationBarcode, lpnBarcode).get();
    }
}