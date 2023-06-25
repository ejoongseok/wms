package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemSize;
import leejoongseok.wms.outbound.exception.OutboundItemIdNotFoundException;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboundTest {

    @Test
    @DisplayName("출고를 분할 한다.")
    void split() {
        final long outboundId = 1L;
        final long outboundItemId = 1L;
        final int outboundItemQuantity = 2;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final Outbound outbound = createSplitTargetOutbound(
                outboundId,
                outboundItemId,
                outboundItemQuantity,
                readyStatus);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityOfSplit);

        final Outbound splittedOutbound = outbound.split(
                List.of(splittableOutboundItem));

        assertThat(splittedOutbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(splittedOutbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
    }

    private Outbound createSplitTargetOutbound(
            final Long outboundId,
            final Long outboundItemId,
            final Integer outboundItemQuantity,
            final OutboundStatus outboundStatus) {
        final OutboundItem outboundItem = createOutboundItem(
                outboundItemId,
                outboundItemQuantity);
        final Outbound outbound = createOutbound(
                outboundStatus,
                outboundId);
        outbound.addOutboundItem(outboundItem);
        return outbound;
    }

    private Outbound createOutbound(
            final OutboundStatus status,
            final Long outboundId) {
        return Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getOutboundStatus), () -> status)
                .supply(Select.field(Outbound::getId), () -> outboundId)
                .supply(Select.field(Outbound::getCushioningMaterial), () -> CushioningMaterial.NONE)
                .supply(Select.field(Outbound::getCushioningMaterialQuantity), () -> 0)
                .ignore(Select.field(Outbound::getOutboundItems))
                .create();
    }

    private OutboundItem createOutboundItem(
            final Long outboundItemId,
            final Integer outboundItemQuantity) {
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getId), () -> outboundItemId)
                .supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundItemQuantity)
                .create();
    }

    private SplittableOutboundItem createSplittableOutboundItem(
            final Long outboundItemIdToSplit,
            final Integer quantityOfSplit) {
        return new SplittableOutboundItem(
                outboundItemIdToSplit,
                quantityOfSplit);
    }

    @Test
    @DisplayName("출고를 분할 한다. [출고 상태가 READY가 아닌 경우]")
    void fail_split_invalid_outbound_status() {
        final long outboundId = 1L;
        final long outboundItemId = 1L;
        final int outboundItemQuantity = 2;
        final OutboundStatus invalidStatus = OutboundStatus.PICKING;
        final Outbound outbound = createSplitTargetOutbound(
                outboundId,
                outboundItemId,
                outboundItemQuantity,
                invalidStatus);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityOfSplit);

        assertThatThrownBy(() -> {
            outbound.split(List.of(splittableOutboundItem));
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("출고는 대기 상태에서만 분할 할 수 있습니다.");
    }

    @Test
    @DisplayName("출고를 분할 한다. [분할 하려는 출고 품목이 존재하지 않는 경우]")
    void fail_split_not_found_target_outbound_item_id() {
        final long outboundId = 1L;
        final long outboundItemId = 1L;
        final int outboundItemQuantity = 2;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final Outbound outbound = createSplitTargetOutbound(
                outboundId,
                outboundItemId,
                outboundItemQuantity,
                readyStatus);

        final Long outboundItemIdToSplit = 2L;
        final Integer quantityOfSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityOfSplit);

        assertThatThrownBy(() -> {
            outbound.split(List.of(splittableOutboundItem));
        }).isInstanceOf(OutboundItemIdNotFoundException.class)
                .hasMessageContaining("출고 상품 ID [2]에 해당하는 출고 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("출고를 분할 한다. [분할 하려는 출고 상품의 수량이 출고 상품의 수량보다 많은 경우]")
    void fail_split_over_split_quantity_target_outbound_item_quantity() {
        final long outboundId = 1L;
        final long outboundItemId = 1L;
        final int outboundItemQuantity = 2;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final Outbound outbound = createSplitTargetOutbound(
                outboundId,
                outboundItemId,
                outboundItemQuantity,
                readyStatus);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 2;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityOfSplit);

        assertThatThrownBy(() -> {
            outbound.split(List.of(splittableOutboundItem));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("분할하려는 상품의 총 수량은 출고 상품의 총 수량보다 작아야 합니다." +
                        "\n분할하려는 상품의 총 수량: 2, 출고 상품의 총 수량: 2\n");
    }

    @Test
    @DisplayName("출고를 분할 한다. [출고 상품 두개 중 하나만 아예 분할하는 경우]")
    void split_1() {
        final long outboundId = 1L;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final OutboundItem outboundItem = createOutboundItem(
                1L,
                2);
        final OutboundItem outboundItem2 = createOutboundItem(
                2L,
                2);
        final Outbound outbound = createOutbound(
                readyStatus,
                outboundId);
        outbound.addOutboundItem(outboundItem);
        outbound.addOutboundItem(outboundItem2);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 2;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityOfSplit);

        final Outbound splittedOutbound = outbound.split(
                List.of(splittableOutboundItem));

        assertThat(splittedOutbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(splittedOutbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(2);
        assertThat(outbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(2);
        assertThat(outbound.getOutboundItems().get(0).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("출고를 분할 한다. [출고 상품 두개 중 하나의 일부 수량만 분할하는 경우]")
    void split_2() {
        final long outboundId = 1L;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final OutboundItem outboundItem = createOutboundItem(
                1L,
                2);
        final OutboundItem outboundItem2 = createOutboundItem(
                2L,
                2);
        final Outbound outbound = createOutbound(
                readyStatus,
                outboundId);
        outbound.addOutboundItem(outboundItem);
        outbound.addOutboundItem(outboundItem2);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityOfSplit);

        final Outbound splittedOutbound = outbound.split(
                List.of(splittableOutboundItem));

        assertThat(splittedOutbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(splittedOutbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().size()).isEqualTo(2);
        assertThat(outbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("출고의 총 부피를 계산한다. (출고의 부피 = 상품의 부피 * 상품의 수량 + 완충재의 부피 * 완충재의 수량)")
    void calculateTotalVolume() {
        final Integer itemLengthInMillimeter = 100;
        final Integer itemWidthInMillimeter = 100;
        final Integer itemHeightInMillimeter = 100;
        final Item item = createItemWithItemSize(
                itemLengthInMillimeter,
                itemWidthInMillimeter,
                itemHeightInMillimeter);
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundWithItemOrQuantity(
                item,
                outboundQuantity);
        final CushioningMaterial cushioningMaterial = CushioningMaterial.BUBBLE_WRAP;
        final Integer cushioningMaterialQuantity = 1;
        final Outbound outbound = createOutboundWithCushioningMaterial(
                cushioningMaterial,
                cushioningMaterialQuantity,
                outboundItem);

        final Long totalVolume = outbound.calculateTotalVolume();

        final int cushioningMaterialVolume = 1000;
        final int itemTotalVolume = 1000000;
        final int outboundTotalVolume = itemTotalVolume + cushioningMaterialVolume;
        assertThat(totalVolume).isEqualTo(outboundTotalVolume);

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

    private Outbound createOutboundWithCushioningMaterial(
            final CushioningMaterial cushioningMaterial,
            final Integer cushioningMaterialQuantity,
            final OutboundItem outboundItem) {
        return Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getCushioningMaterial),
                        () -> cushioningMaterial)
                .supply(Select.field(Outbound::getCushioningMaterialQuantity),
                        () -> cushioningMaterialQuantity)
                .supply(Select.field(Outbound::getOutboundItems),
                        () -> List.of(outboundItem))
                .ignore(Select.field(Outbound::getRecommendedPackagingMaterial))
                .create();
    }

    @Test
    @DisplayName("출고의 총 무게를 계산한다. (출고의 무게 = 상품의 무게 * 상품의 수량 + 완충재의 무게 * 완충재의 수량)")
    void calculateTotalWeightInGrams() {
        final Integer itemWeightInGrams = 100;
        final Item item = createItemWithItemWeight(itemWeightInGrams);
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundWithItemOrQuantity(
                item,
                outboundQuantity);
        final CushioningMaterial cushioningMaterial = CushioningMaterial.BUBBLE_WRAP;
        final Integer cushioningMaterialQuantity = 1;
        final Outbound outbound = createOutboundWithCushioningMaterial(
                cushioningMaterial,
                cushioningMaterialQuantity,
                outboundItem);

        final Long totalWeightInGrams = outbound.calculateTotalWeightInGrams();

        final int cushioningMaterialWeightInGrams = 10;
        final int itemTotalWeightInGrams = 100;
        final int outboundTotalWeightInGrams =
                itemTotalWeightInGrams + cushioningMaterialWeightInGrams;
        assertThat(totalWeightInGrams).isEqualTo(outboundTotalWeightInGrams);
    }

    private Item createItemWithItemWeight(
            final Integer itemWeightInGrams) {
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getWeightInGrams), () -> itemWeightInGrams)
                .create();
    }
}