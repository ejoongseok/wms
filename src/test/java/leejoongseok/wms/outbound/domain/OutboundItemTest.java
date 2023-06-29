package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemSize;
import leejoongseok.wms.location.domain.LocationLPN;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboundItemTest {

    @Test
    @DisplayName("출고 상품을 분할 한다.")
    void split() {
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);

        final int quantityToSplit = 1;
        final OutboundItem splittedOutboundItem = outboundItem.split(quantityToSplit);
        assertThat(splittedOutboundItem.getOutboundQuantity()).isEqualTo(1);
        assertThat(outboundItem.getItem()).isEqualTo(splittedOutboundItem.getItem());
        assertThat(outboundItem.getUnitPrice()).isEqualTo(splittedOutboundItem.getUnitPrice());
    }

    private OutboundItem createOutboundItem(final Integer outboundQuantity) {
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundQuantity)
                .create();
    }

    @Test
    @DisplayName("출고 상품을 분할 한다.[ 실패 : 분할 수량이 출고 수량보다 많을 경우 ]")
    void fail_split_invalid_quantity_of_split() {
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);

        final int overQuantityToSplit = 2;
        assertThatThrownBy(() -> {
            outboundItem.split(overQuantityToSplit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("분할 수량은 출고 수량보다 작거나 같아야 합니다. 출고 수량: 1, 분할 수량: 2");
    }

    @Test
    @DisplayName("출고 상품을 분할 한다.[ 실패 : 분할 수량 0 이하 ]")
    void fail_split_zero_quantity_of_split() {
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);

        final int zeroQuantityToSplit = 0;
        assertThatThrownBy(() -> {
            outboundItem.split(zeroQuantityToSplit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출고 수량은 0보다 커야합니다.");
    }

    @Test
    @DisplayName("출고 상품의 부피를 계산한다. (출고 상품의 부피 = 상품의 부피 * 출고 수량)")
    void calculateVolume() {
        final Integer itemLengthInMillimeters = 100;
        final Integer itemWidthInMillimeters = 100;
        final Integer itemHeightInMillimeters = 100;
        final Item item = createItemWithItemSize(
                itemLengthInMillimeters,
                itemWidthInMillimeters,
                itemHeightInMillimeters);
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundWithItemOrQuantity(
                item,
                outboundQuantity);

        final Long outboundItemVolume = outboundItem.calculateVolume();

        final Long itemVolume = 100L * 100L * 100L;
        final Long totalVolume = itemVolume * outboundQuantity;
        assertThat(outboundItemVolume).isEqualTo(totalVolume);
    }

    private Item createItemWithItemSize(
            final Integer itemLengthInMillimeter,
            final Integer itemWidthInMillimeter,
            final Integer itemHeightInMillimeter) {
        final ItemSize itemSize = Instancio.of(ItemSize.class)
                .supply(Select.field(ItemSize::getLengthInMillimeters), () -> itemLengthInMillimeter)
                .supply(Select.field(ItemSize::getWidthInMillimeters), () -> itemWidthInMillimeter)
                .supply(Select.field(ItemSize::getHeightInMillimeters), () -> itemHeightInMillimeter)
                .create();
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getItemSize), () -> itemSize)
                .create();
    }

    private OutboundItem createOutboundWithItemOrQuantity(
            final Item item,
            final Integer outboundQuantity) {
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundQuantity)
                .supply(Select.field(OutboundItem::getItem), () -> item)
                .create();
    }

    @Test
    @DisplayName("출고 상품의 무게를 계산한다. (출고 상품의 무게 = 상품의 무게 * 출고 수량)")
    void calculateWeightInGrams() {
        final Integer itemWeightInGrams = 100;
        final Integer outboundQuantity = 2;
        final Item item = createItem(itemWeightInGrams);
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity, item);

        final Long outboundItemWeightInGrams = outboundItem.calculateWeightInGrams();

        assertThat(outboundItemWeightInGrams).isEqualTo(200L);
    }

    private Item createItem(final Integer itemWeightInGrams) {
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getWeightInGrams), () -> itemWeightInGrams)
                .create();
    }

    private OutboundItem createOutboundItem(
            final Integer outboundQuantity,
            final Item item) {
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundQuantity)
                .supply(Select.field(OutboundItem::getItem), () -> item)
                .create();
    }

    @Test
    @DisplayName("입력한 수량만큼 출고수량이 감소한다.")
    void decreaseQuantity() {
        final Integer outboundQuantity = 2;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);
        final Integer decreaseQuantity = 1;

        outboundItem.decreaseQuantity(decreaseQuantity);

        assertThat(outboundItem.getOutboundQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("입력한 수량만큼 출고수량이 감소한다. 감소할 수량이 현재 수량보다 많을 경우 예외가 발생한다.")
    void decreaseQuantity_fail_over_decrease_quantity() {
        final Integer outboundQuantity = 2;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);
        final Integer decreaseQuantity = 3;

        assertThatThrownBy(() -> {
            outboundItem.decreaseQuantity(decreaseQuantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "감소할 수량은 출고 수량보다 작거나 같아야 합니다. " +
                                "출고 수량: 2, 감소 수량: 3");
    }

    @Test
    @DisplayName("입력한 수량만큼 출고수량이 감소한다. 감소할 수량이 0보다 작을경우 예외가 발생한다.")
    void decreaseQuantity_fail_min_decrease_quantity() {
        final Integer outboundQuantity = 2;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);
        final Integer decreaseQuantity = 0;

        assertThatThrownBy(() -> {
            outboundItem.decreaseQuantity(decreaseQuantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "감소 수량은 0보다 커야합니다.");
    }

    @Test
    @DisplayName("출고 상품에 할당된 집품 목록만큼 LocationLPN의 재고를 차감한다.")
    void deductAllocatedInventory() {
        final int inventoryQuantity = 10;
        final LocationLPN locationLPN = createLocationLPN(inventoryQuantity);
        final int quantityRequiredForPick = 5;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(quantityRequiredForPick, locationLPN, pickingStatus);
        final OutboundItem outboundItem = createOutboundItem(List.of(picking));

        outboundItem.deductAllocatedInventory();

        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(5);
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

    private OutboundItem createOutboundItem(final List<Picking> pickings) {
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getPickings), () -> pickings)
                .create();
    }


    @Test
    @DisplayName("출고 상품에 할당된 집품 목록만큼 LocationLPN의 재고를 차감한다. - pickings가 비어있을 경우 예외가 발생한다.")
    void deductAllocatedInventory_empty_pickings() {
        final OutboundItem outboundItem = createOutboundItem(List.of());

        assertThatThrownBy(() -> {
            outboundItem.deductAllocatedInventory();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품 목록이 비어있습니다.");

    }

    @Test
    @DisplayName("출고 상품에 할당된 집품 목록만큼 LocationLPN의 재고를 차감한다. - 차감해야할 수량이 재고보다 많을 경우 예외가 발생한다.")
    void deductAllocatedInventory_over_deduct() {
        final int inventoryQuantity = 10;
        final LocationLPN locationLPN = createLocationLPN(inventoryQuantity);
        final int quantityRequiredForPick = 50;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(quantityRequiredForPick, locationLPN, pickingStatus);
        final OutboundItem outboundItem = createOutboundItem(List.of(picking));

        assertThatThrownBy(() -> {
            outboundItem.deductAllocatedInventory();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("차감할 재고 수량이 재고 수량보다 많습니다. 재고 수량: 10, 차감할 재고 수량: 50");
    }

    @Test
    @DisplayName("출고상품에 집품이 모두 완료되었는지 확인한다.")
    void isCompletedPicking() {
        final int inventoryQuantity = 10;
        final LocationLPN locationLPN = createLocationLPN(inventoryQuantity);
        final int quantityRequiredForPick = 5;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(quantityRequiredForPick, locationLPN, pickingStatus);
        final OutboundItem outboundItem = createOutboundItem(List.of(picking));
        picking.addManualPickedQuantity(locationLPN, 5);

        final boolean isCompletedPicking = outboundItem.isCompletedPicking();

        assertThat(isCompletedPicking).isTrue();
    }

    @Test
    @DisplayName("출고상품에 집품이 모두 완료되었는지 확인한다. - 집품이 완료되지 않았을 경우")
    void isCompletedPicking_not_completed() {
        final int inventoryQuantity = 10;
        final LocationLPN locationLPN = createLocationLPN(inventoryQuantity);
        final int quantityRequiredForPick = 5;
        final PickingStatus pickingStatus = PickingStatus.READY;
        final Picking picking = createPicking(quantityRequiredForPick, locationLPN, pickingStatus);
        final OutboundItem outboundItem = createOutboundItem(List.of(picking));
        picking.addManualPickedQuantity(locationLPN, 4);

        final boolean isCompletedPicking = outboundItem.isCompletedPicking();

        assertThat(isCompletedPicking).isFalse();
    }
}