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

    @Test
    @DisplayName("로케이션 LPN이 집품 가능한지 확인한다.")
    void isPickingAllocatable() {
        final UsagePurpose usagePurpose = UsagePurpose.STOW;
        final Location location = createLocation(usagePurpose);
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1);
        final LPN lpn = createLPN(expirationAt);
        final Integer inventoryQuantity = 1;
        final LocationLPN locationLPN = createLocationLPN(location, lpn, inventoryQuantity);

        final boolean isPickingAllocatable = locationLPN.isPickingAllocatable(LocalDateTime.now());

        assertThat(isPickingAllocatable).isTrue();
    }

    private Location createLocation(final UsagePurpose usagePurpose) {
        return Instancio.of(Location.class)
                .supply(Select.field(Location::getUsagePurpose), () -> usagePurpose)
                .create();
    }

    private LPN createLPN(final LocalDateTime expirationAt) {
        return Instancio.of(LPN.class)
                .supply(Select.field(LPN::getExpirationAt), () -> expirationAt)
                .create();
    }

    private LocationLPN createLocationLPN(
            final Location location,
            final LPN lpn,
            final Integer inventoryQuantity) {
        return Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getLpn), () -> lpn)
                .supply(Select.field(LocationLPN::getLocation), () -> location)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> inventoryQuantity)
                .create();
    }

    @Test
    @DisplayName("로케이션 LPN이 집품 가능한지 확인한다. - 유통기한 지남")
    void isPickingAllocatable_expiredLPN() {
        final UsagePurpose usagePurpose = UsagePurpose.STOW;
        final Location location = createLocation(usagePurpose);
        final LocalDateTime expirationAt = LocalDateTime.now().minusDays(1);
        final LPN lpn = createLPN(expirationAt);
        final Integer inventoryQuantity = 1;
        final LocationLPN locationLPN = createLocationLPN(location, lpn, inventoryQuantity);

        final boolean isPickingAllocatable = locationLPN.isPickingAllocatable(LocalDateTime.now());

        assertThat(isPickingAllocatable).isFalse();
    }

    @Test
    @DisplayName("로케이션 LPN이 집품 가능한지 확인한다. - 사용 용도가 진열이 아님")
    void isPickingAllocatable_invalidUsagePurpose() {
        final UsagePurpose usagePurpose = UsagePurpose.MOVE;
        final Location location = createLocation(usagePurpose);
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1);
        final LPN lpn = createLPN(expirationAt);
        final Integer inventoryQuantity = 1;
        final LocationLPN locationLPN = createLocationLPN(location, lpn, inventoryQuantity);

        final boolean isPickingAllocatable = locationLPN.isPickingAllocatable(LocalDateTime.now());

        assertThat(isPickingAllocatable).isFalse();
    }

    @Test
    @DisplayName("로케이션 LPN이 집품 가능한지 확인한다. - 재고 수량이 0")
    void isPickingAllocatable_inventoryQuantity() {
        final UsagePurpose usagePurpose = UsagePurpose.STOW;
        final Location location = createLocation(usagePurpose);
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1);
        final LPN lpn = createLPN(expirationAt);
        final Integer inventoryQuantity = 0;
        final LocationLPN locationLPN = createLocationLPN(location, lpn, inventoryQuantity);

        final boolean isPickingAllocatable = locationLPN.isPickingAllocatable(LocalDateTime.now());

        assertThat(isPickingAllocatable).isFalse();
    }

    @Test
    @DisplayName("집품에 필요한 수량만큼 재고를 차감한다.")
    void deductInventory() {
        final LocationLPN locationLPN = Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> 10)
                .create();
        final int quantityRequiredForPick = 5;

        locationLPN.deductInventory(quantityRequiredForPick);

        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("집품에 필요한 수량만큼 재고를 차감한다. - 차감할 수량이 재고 수량보다 많음")
    void deductInventory_over_pick_quantity() {
        final LocationLPN locationLPN = Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> 10)
                .create();
        final int quantityRequiredForPick = 50;

        assertThatThrownBy(() -> {
            locationLPN.deductInventory(quantityRequiredForPick);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("차감할 재고 수량이 재고 수량보다 많습니다. 재고 수량: 10, 차감할 재고 수량: 50");
    }
}