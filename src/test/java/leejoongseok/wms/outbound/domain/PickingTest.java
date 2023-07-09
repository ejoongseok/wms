package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.common.fixture.LocationLPNFixture;
import leejoongseok.wms.common.fixture.PickingFixture;
import leejoongseok.wms.location.domain.LocationLPN;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PickingTest {

    @Test
    @DisplayName("집품한 상품이 있는지 확인한다.")
    void hasPickedItem() {
        final Picking picking = PickingFixture.aPicking()
                .withPickedQuantity(1)
                .build();

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
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(inventoryQuantity)
                .build();
        final int quantityRequiredForPick = 5;
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(quantityRequiredForPick)
                .withLocationLPN(locationLPN)
                .build();

        picking.deductAllocatedInventory();

        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(inventoryQuantity - quantityRequiredForPick);
    }


    @Test
    @DisplayName("집품에 필요한 수량만큼 LocationLPN 재고를 차감한다. - 집품의 상태가 READY가 아님")
    void deductAllocatedInventory_invalidStatus() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final Picking picking = PickingFixture.aPickingWithInProgressPickingNoPickedQuantity()
                .withQuantityRequiredForPick(5)
                .withLocationLPN(locationLPN)
                .build();

        assertThatThrownBy(() -> {
            picking.deductAllocatedInventory();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품에 할당된 LocationLPN의 재고를 차감하기위해서는 집품을 시작하기 전이어야 합니다.");
    }

    @Test
    @DisplayName("집품에 필요한 수량만큼 LocationLPN 재고를 차감한다. - 차감해야할 수량이 재고보다 많음")
    void deductAllocatedInventory_over_deduct_quantity() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(50)
                .withLocationLPN(locationLPN)
                .build();

        assertThatThrownBy(() -> {
            picking.deductAllocatedInventory();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("차감할 재고 수량이 재고 수량보다 많습니다. 재고 수량: 10, 차감할 재고 수량: 50");
    }

    @Test
    @DisplayName("집품한 수량을 증가시킨다.")
    void increasePickedQuantity() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(2)
                .withLocationLPN(locationLPN)
                .build();

        picking.increasePickedQuantity(locationLPN);

        assertThat(picking.getPickedQuantity()).isEqualTo(1);
        assertThat(picking.isInProgress()).isTrue();
    }

    @Test
    @DisplayName("집품한 수량을 증가시킨다. - 집품완료")
    void increasePickedQuantity_completedPicking() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(2)
                .withLocationLPN(locationLPN)
                .build();

        picking.increasePickedQuantity(locationLPN);
        assertThat(picking.isInProgress()).isTrue();
        picking.increasePickedQuantity(locationLPN);

        assertThat(picking.getPickedQuantity()).isEqualTo(2);
        assertThat(picking.isCompletedPicking()).isTrue();

    }
    @Test
    @DisplayName("집품한 수량을 증가시킨다. - 집품해야할 LocatinLPN이 아님")
    void increasePickedQuantity_not_match_locationLPN() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final LocationLPN locationLPN2 = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(1)
                .withLocationLPN(locationLPN)
                .build();

        assertThatThrownBy(() -> {
            picking.increasePickedQuantity(locationLPN2);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품에 할당된 LocationLPN이 아닌 LocationLPN의 수량을 증가시킬 수 없습니다.");
    }

    @Test
    @DisplayName("집품한 수량을 증가시킨다. - 이미 집품이 완료된 상태")
    void increasePickedQuantity_invalid_status() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithCompletedPickingNoPickedQuantity()
                .withQuantityRequiredForPick(1)
                .withLocationLPN(locationLPN)
                .build();

        assertThatThrownBy(() -> {
            picking.increasePickedQuantity(locationLPN);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 완료된 집품은 집품 수량을 증가시킬 수 없습니다");
    }

    @Test
    @DisplayName("집품 수량을 직접 입력한 수량만큼 증가시킵니다.")
    void addManualPickedQuantity() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(2)
                .withLocationLPN(locationLPN)
                .build();

        picking.addManualPickedQuantity(locationLPN, 1);

        assertThat(picking.getPickedQuantity()).isEqualTo(1);
        assertThat(picking.isInProgress()).isTrue();
    }

    @Test
    @DisplayName("집품 수량을 직접 입력한 수량만큼 증가시킵니다. - 집품을 완료시킵니다.")
    void addManualPickedQuantity_complete() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(2)
                .withLocationLPN(locationLPN)
                .build();

        picking.addManualPickedQuantity(locationLPN, 2);

        assertThat(picking.getPickedQuantity()).isEqualTo(2);
        assertThat(picking.isCompletedPicking()).isTrue();
    }

    @Test
    @DisplayName("집품 수량을 직접 입력한 수량만큼 증가시킵니다. - 집품을 완료시킵니다.2")
    void addManualPickedQuantity_complete2() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(2)
                .withLocationLPN(locationLPN)
                .build();

        picking.addManualPickedQuantity(locationLPN, 1);
        picking.addManualPickedQuantity(locationLPN, 1);

        assertThat(picking.getPickedQuantity()).isEqualTo(2);
        assertThat(picking.isCompletedPicking()).isTrue();
    }

    @Test
    @DisplayName("집품 수량을 직접 입력한 수량만큼 증가시킵니다. - 이미 집품이 완료된 상태")
    void addManualPickedQuantity_already_compelted() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithCompletedPickingNoPickedQuantity()
                .withQuantityRequiredForPick(2)
                .withLocationLPN(locationLPN)
                .build();

        assertThatThrownBy(() -> {
            picking.addManualPickedQuantity(locationLPN, 1);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 완료된 집품은 집품 수량을 증가시킬 수 없습니다.");
    }

    @Test
    @DisplayName("집품 수량을 직접 입력한 수량만큼 증가시킵니다. - 집품해야할 LocatinLPN이 아님")
    void addManualPickedQuantity_not_match_locationLPN() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final LocationLPN locationLPN2 = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(1)
                .withLocationLPN(locationLPN)
                .build();

        assertThatThrownBy(() -> {
            picking.addManualPickedQuantity(locationLPN2, 1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품에 할당된 LocationLPN이 아닌 LocationLPN의 수량을 증가시킬 수 없습니다.");
    }

    @Test
    @DisplayName("집품 수량을 직접 입력한 수량만큼 증가시킵니다. - 집품해야할 수량보다 많이 입력")
    void addManualPickedQuantity_over_quantity() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(2)
                .withLocationLPN(locationLPN)
                .build();

        assertThatThrownBy(() -> {
            picking.addManualPickedQuantity(locationLPN, 3);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품해야할 수량보다 집품하려는 수량이 많습니다. 집품해야할 수량: 2, 입력한 수량: 3");
    }

    @Test
    @DisplayName("집품 수량을 직접 입력한 수량만큼 증가시킵니다. - 집품해야할 수량보다 많이 입력2")
    void addManualPickedQuantity_over_quantity2() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN().build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(2)
                .withLocationLPN(locationLPN)
                .build();
        picking.addManualPickedQuantity(locationLPN, 1);
        assertThatThrownBy(() -> {
            picking.addManualPickedQuantity(locationLPN, 2);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품해야할 수량보다 집품하려는 수량이 많습니다. 집품해야할 수량: 2, 현재 집품한 수량: 1, 추가로 입력한 수량: 2");
    }
}