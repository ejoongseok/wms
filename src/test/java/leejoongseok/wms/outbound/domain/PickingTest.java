package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PickingTest {

    @Test
    @DisplayName("집품한 상품이 있는지 확인한다.")
    void hasPickedItem() {
        final Picking picking = Instancio.of(Picking.class)
                .supply(Select.field(Picking::getPickedQuantity), () -> 1)
                .create();

        final boolean hasPickedItem = picking.hasPickedItem();

        assertThat(hasPickedItem).isTrue();
    }

    @Test
    @DisplayName("집품한 상품이 있는지 확인한다. - 집품한 상품이 없음")
    void hasPickedItem_not_picked() {
        final Picking picking = new Picking();

        final boolean hasPickedItem = picking.hasPickedItem();

        assertThat(hasPickedItem).isFalse();
    }

    @Test
    @DisplayName("집품에 필요한 수량만큼 LocationLPN 재고를 차감한다.")
    void deductAllocatedInventory() {
        final int inventoryQuantity = 10;
        final LocationLPN locationLPN = createLocationLPN(inventoryQuantity);
        final int quantityRequiredForPick = 5;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(quantityRequiredForPick, locationLPN, pickingStatus);

        picking.deductAllocatedInventory();

        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(inventoryQuantity - quantityRequiredForPick);
    }

    private LocationLPN createLocationLPN(final int inventoryQuantity) {
        return Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> inventoryQuantity)
                .create();
    }

    private Picking createPicking(
            final int quantityRequiredForPick,
            final LocationLPN locationLPN,
            final PickingStatus pickingStatus) {
        return Instancio.of(Picking.class)
                .supply(Select.field(Picking::getQuantityRequiredForPick), () -> quantityRequiredForPick)
                .supply(Select.field(Picking::getStatus), () -> pickingStatus)
                .supply(Select.field(Picking::getLocationLPN), () -> locationLPN)
                .ignore(Select.field(Picking::getPickedQuantity))
                .create();
    }


    @Test
    @DisplayName("집품에 필요한 수량만큼 LocationLPN 재고를 차감한다. - 집품의 상태가 READY가 아님")
    void deductAllocatedInventory_invalidStatus() {
        final int inventoryQuantity = 10;
        final LocationLPN locationLPN = createLocationLPN(inventoryQuantity);
        final int quantityRequiredForPick = 5;
        final PickingStatus pickingStatus = PickingStatus.PROCESSING;
        final Picking picking = createPicking(quantityRequiredForPick, locationLPN, pickingStatus);

        assertThatThrownBy(() -> {
            picking.deductAllocatedInventory();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품에 할당된 LocationLPN의 재고를 차감하기위해서는 집품을 시작하기 전이어야 합니다.");
    }

    @Test
    @DisplayName("집품에 필요한 수량만큼 LocationLPN 재고를 차감한다. - 차감해야할 수량이 재고보다 많음")
    void deductAllocatedInventory_over_deduct_quantity() {
        final int inventoryQuantity = 10;
        final LocationLPN locationLPN = createLocationLPN(inventoryQuantity);
        final int quantityRequiredForPick = 50;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(quantityRequiredForPick, locationLPN, pickingStatus);

        assertThatThrownBy(() -> {
            picking.deductAllocatedInventory();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("차감할 재고 수량이 재고 수량보다 많습니다. 재고 수량: 10, 차감할 재고 수량: 50");
    }

    @Test
    @DisplayName("집품한 수량을 증가시킨다.")
    void increasePickedQuantity() {
        final LocationLPN locationLPN = Instancio.create(LocationLPN.class);
        final int quantityRequiredForPick = 1;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(
                quantityRequiredForPick,
                locationLPN,
                pickingStatus);

        picking.increasePickedQuantity(locationLPN);

        assertThat(picking.getPickedQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("집품한 수량을 증가시킨다. 집품해야할 수량보다 집품하려는 수량이 많음")
    void increasePickedQuantity_over_pick() {
        final LocationLPN locationLPN = Instancio.create(LocationLPN.class);
        final int quantityRequiredForPick = 1;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(
                quantityRequiredForPick,
                locationLPN,
                pickingStatus);

        picking.increasePickedQuantity(locationLPN);
        assertThatThrownBy(() -> {
            picking.increasePickedQuantity(locationLPN);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품해야할 수량보다 집품하려는 수량이 많습니다.");
    }

    @Test
    @DisplayName("집품한 수량을 증가시킨다. - 집품해야할 LocatinLPN이 아님")
    void increasePickedQuantity_not_match_locationLPN() {
        final LocationLPN locationLPN = Instancio.create(LocationLPN.class);
        final LocationLPN locationLPN2 = Instancio.create(LocationLPN.class);
        final int quantityRequiredForPick = 1;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(
                quantityRequiredForPick,
                locationLPN,
                pickingStatus);

        assertThatThrownBy(() -> {
            picking.increasePickedQuantity(locationLPN2);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품에 할당된 LocationLPN이 아닌 LocationLPN의 수량을 증가시킬 수 없습니다.");
    }

    @Test
    @DisplayName("집품한 수량을 증가시킨다. - 이미 집품이 완료된 상태")
    void increasePickedQuantity_invalid_status() {
        final LocationLPN locationLPN = Instancio.create(LocationLPN.class);
        final int quantityRequiredForPick = 1;
        final PickingStatus pickingStatus = PickingStatus.COMPLETED;
        final Picking picking = createPicking(
                quantityRequiredForPick,
                locationLPN,
                pickingStatus);

        assertThatThrownBy(() -> {
            picking.increasePickedQuantity(locationLPN);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 완료된 집품은 집품 수량을 증가시킬 수 없습니다");
    }
}