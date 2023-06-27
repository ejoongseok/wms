package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PickingCreatorTest {

    private PickingCreator pickingCreator;

    @BeforeEach
    void setUp() {
        pickingCreator = new PickingCreator();
    }

    @Test
    @DisplayName("재고가 집품해야할 수량보다 충분하면 출고상품의 집품목록을 생성한다.")
    void createPickings() {
        final long itemId = 1L;
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(itemId, 3, "locationBarcode-3", 3, LocalDateTime.now().plusDays(1L)),
                createLocationLPN(itemId, 2, "locationBarcode-2", 2, LocalDateTime.now().plusDays(1L)),
                createLocationLPN(itemId, 1, "locationBarcode-1", 1, LocalDateTime.now().plusDays(1L))
        );

        final List<Picking> pickings = pickingCreator.createPickings(1L, 5, locationLPNList);
        assertThat(pickings).hasSize(2);
        assertThat(pickings.get(0).getLocationLPN().getLocationBarcode()).isEqualTo("locationBarcode-3");
        assertThat(pickings.get(1).getLocationLPN().getLocationBarcode()).isEqualTo("locationBarcode-2");
    }

    private LocationLPN createLocationLPN(
            final Long itemId,
            final long locationLPNId,
            final String locationBarcode,
            final int inventoryQuantity,
            final LocalDateTime expirationAt) {
        final LPN lpn = Instancio.of(LPN.class)
                .supply(Select.field(LPN::getExpirationAt), () -> expirationAt)
                .supply(Select.field(LPN::getItemId), () -> itemId)
                .create();
        final Location location = Instancio.of(Location.class)
                .supply(Select.field(Location::getLocationBarcode), () -> locationBarcode)
                .create();
        return Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getId), () -> locationLPNId)
                .supply(Select.field(LocationLPN::getLpn), () -> lpn)
                .supply(Select.field(LocationLPN::getLocation), () -> location)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> inventoryQuantity)
                .supply(Select.field(LocationLPN::getItemId), () -> itemId)
                .create();
    }
}