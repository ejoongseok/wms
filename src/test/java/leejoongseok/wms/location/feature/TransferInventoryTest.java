package leejoongseok.wms.location.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class TransferInventoryTest extends ApiTest {

    @Autowired
    private TransferInventory transferInventory;
    @Autowired
    private LocationLPNRepository locationLPNRepository;

    @Test
    @DisplayName("A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능")
    void transferInventory() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .createLocation()
                .locationBarcode("toLocationBarcode")
                .request()
                .assignLPNToLocation().request()
                .addManualInventoryToLocationLPN()
                .inventoryQuantity(10)
                .request();

        final String fromLocationBarcode = "A1-1-1";
        final String toLocationBarcode = "toLocationBarcode";
        final Long targetLPNId = 1L;
        final Integer transferQuantity = 5;
        final TransferInventory.Request request = new TransferInventory.Request(
                fromLocationBarcode,
                toLocationBarcode,
                targetLPNId,
                transferQuantity
        );
        transferInventory.request(request);

        final LocationLPN fromLocationLPN = getLocationLPN(fromLocationBarcode);
        assertThat(fromLocationLPN.getInventoryQuantity()).isEqualTo(6);
        final LocationLPN toLocationLPN = getLocationLPN(toLocationBarcode);
        assertThat(toLocationLPN.getInventoryQuantity()).isEqualTo(5);
    }

    private LocationLPN getLocationLPN(final String fromLocationBarcode) {
        return locationLPNRepository.testingFindByLocationBarcode(fromLocationBarcode).get();
    }
}