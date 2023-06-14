package leejoongseok.wms.location.domain;

import leejoongseok.wms.inbound.domain.LPN;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocationLPNTest {

    @Test
    @DisplayName("로케이션 LPN의 재고 수량을 1씩 증가시킨다.")
    void incrementInventoryQuantity() {
        final LocationLPN locationLPN = new LocationLPN();
        final Integer inventoryQuantity = locationLPN.getInventoryQuantity();

        locationLPN.incrementInventoryQuantity();

        assertThat(inventoryQuantity).isEqualTo(1);
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량을 입력한 숫자만큼 증가시킨다.")
    void addManualInventoryQuantity() {
        final LocationLPN locationLPN = new LocationLPN();
        final Integer inventoryQuantity = locationLPN.getInventoryQuantity();

        locationLPN.addManualInventoryQuantity(10);

        assertThat(inventoryQuantity).isEqualTo(1);
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(11);
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량을 입력한 숫자만큼 증가시킨다. [실패 - 증가시키려는 재고 수량이 0 이하]")
    void fail_invalid_quantity_addManualInventoryQuantity() {
        final LocationLPN locationLPN = new LocationLPN();

        assertThatThrownBy(() -> {
            locationLPN.addManualInventoryQuantity(0);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("추가할 재고 수량은 1이상이어야 합니다.");
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다. [해당일자는 유통기한 전임.]")
    void isFreshBy() {
        final LocationLPN locationLPN = createLocationLPN();
        final LocalDateTime thisDateTime = LocalDateTime.now().minusDays(1);

        final boolean isFresh = locationLPN.isFreshLPNBy(thisDateTime);

        assertThat(isFresh).isTrue();
    }

    private LocationLPN createLocationLPN() {
        final LPN validLPN = Instancio.of(LPN.class)
                .supply(Select.field(LPN::getExpirationAt), () -> LocalDateTime.now())
                .create();
        return Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getLpn), () -> validLPN)
                .create();
    }

    @Test
    @DisplayName("LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다. [해당 일자는 유통기한을 지남.]")
    void isExpired() {
        final LocationLPN locationLPN = createLocationLPN();
        final LocalDateTime thisDateTime = LocalDateTime.now().plusDays(1);

        final boolean isFresh = locationLPN.isFreshLPNBy(thisDateTime);

        assertThat(isFresh).isFalse();
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량이 비어있는지 확인한다.")
    void isEmptyIventory() {
        final LocationLPN locationLPN = Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> 0)
                .create();

        final boolean isEmpty = locationLPN.isEmptyInventory();

        assertThat(isEmpty).isTrue();
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량이 비어있는지 확인한다. [비어있지않음.]")
    void fail_isEmptyIventory() {
        final LocationLPN locationLPN = new LocationLPN();

        final boolean isEmpty = locationLPN.isEmptyInventory();

        assertThat(isEmpty).isFalse();
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(1);
    }
}