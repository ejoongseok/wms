package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemSize;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboundItemTest {

    @Test
    @DisplayName("출고 상품을 분할 한다.")
    void split() {
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);

        final int quantityOfSplit = 1;
        final OutboundItem splittedOutboundItem = outboundItem.split(quantityOfSplit);
        assertThat(outboundItem.getOutboundQuantity()).isZero();
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

        final int overQuantityOfSplit = 2;
        assertThatThrownBy(() -> {
            outboundItem.split(overQuantityOfSplit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("분할 수량은 출고 수량보다 작거나 같아야 합니다.출고 수량: 1, 분할 수량: 2");
    }

    @Test
    @DisplayName("출고 상품을 분할 한다.[ 실패 : 분할 수량 0 이하 ]")
    void fail_split_zero_quantity_of_split() {
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);

        final int zeroQuantityOfSplit = 0;
        assertThatThrownBy(() -> {
            outboundItem.split(zeroQuantityOfSplit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출고 수량은 0보다 커야합니다.");
    }

    @Test
    @DisplayName("출고 상품의 부피를 계산한다. (출고 상품의 부피 = 상품의 부피 * 출고 수량)")
    void calculateVolume() {
        final Integer itemLengthMillimeter = 100;
        final Integer itemWidthMillimeter = 100;
        final Integer itemHeightMillimeter = 100;
        final Item item = createItemWithItemSize(
                itemLengthMillimeter,
                itemWidthMillimeter,
                itemHeightMillimeter);
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
            final Integer itemLengthMillimeter,
            final Integer itemWidthMillimeter,
            final Integer itemHeightMillimeter) {
        final ItemSize itemSize = Instancio.of(ItemSize.class)
                .supply(Select.field(ItemSize::getLengthMillimeter), () -> itemLengthMillimeter)
                .supply(Select.field(ItemSize::getWidthMillimeter), () -> itemWidthMillimeter)
                .supply(Select.field(ItemSize::getHeightMillimeter), () -> itemHeightMillimeter)
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
}