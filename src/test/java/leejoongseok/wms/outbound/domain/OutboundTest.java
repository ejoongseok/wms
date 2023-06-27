package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemSize;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.StorageType;
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
        final Integer quantityToSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityToSplit);

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
                .ignore(Select.field(Outbound::getToteLocation))
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
            final Integer quantityToSplit) {
        return new SplittableOutboundItem(
                outboundItemIdToSplit,
                quantityToSplit);
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
        final Integer quantityToSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityToSplit);

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
        final Integer quantityToSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityToSplit);

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
        final Integer quantityToSplit = 2;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityToSplit);

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
        final Integer quantityToSplit = 2;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityToSplit);

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
        final Integer quantityToSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = createSplittableOutboundItem(
                outboundItemIdToSplit,
                quantityToSplit);

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

    @Test
    @DisplayName("출고의 상태가 출고 대기 인지 확인한다.")
    void isReadyStatus() {
        final long outboundId = 1L;
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status, outboundId);

        final boolean isReadyStatus = outbound.isReadyStatus();

        assertThat(isReadyStatus).isTrue();
    }

    @Test
    @DisplayName("출고의 상태가 출고 대기 인지 확인한다. - 출고 대기가 아닌 경우 isReadyStatus는 false를 반환한다.")
    void isNotReadyStatus() {
        final long outboundId = 1L;
        final OutboundStatus status = OutboundStatus.PICKING;
        final Outbound outbound = createOutbound(status, outboundId);

        final boolean isReadyStatus = outbound.isReadyStatus();

        assertThat(isReadyStatus).isFalse();
    }

    @Test
    @DisplayName("출고에 토트가 배정되어 있는 상태인지 확인한다.")
    void hasAssignedTote() {
        final Location toteLocation = Instancio.create(Location.class);
        final Outbound outbound = createOutbound(toteLocation);

        final boolean hasAssignedTote = outbound.hasAssignedTote();

        assertThat(hasAssignedTote).isTrue();
    }

    private Outbound createOutbound(final Location toteLocation) {
        return Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getToteLocation), () -> toteLocation)
                .create();
    }

    @Test
    @DisplayName("출고에 토트가 배정되어 있는 상태인지 확인한다. - 토트가 배정되어 있지 않은 경우 hasAssignedTote는 false를 반환한다.")
    void hasAssignedTote_false() {
        final Location toteLocation = null;
        final Outbound outbound = createOutbound(toteLocation);

        final boolean hasAssignedTote = outbound.hasAssignedTote();

        assertThat(hasAssignedTote).isFalse();
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다.")
    void assignPickingTote() {
        final long outboundId = 1L;
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status, outboundId);
        final List<LocationLPN> locationLPNList = List.of();
        final Location toteLocation = createToteLocation(
                locationLPNList,
                StorageType.TOTE);

        outbound.assignPickingTote(toteLocation);

        assertThat(outbound.hasAssignedTote()).isTrue();
    }

    private Location createToteLocation(
            final List<LocationLPN> locationLPNList,
            final StorageType storageType) {
        return Instancio.of(Location.class)
                .supply(Select.field(Location::getStorageType), () -> storageType)
                .supply(Select.field(Location::getLocationLPNList), () -> locationLPNList)
                .create();
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 로케이션이 토트가 아닌 경우 IllegalArgumentException이 발생한다.")
    void assignPickingTote_isNotTote() {
        final long outboundId = 1L;
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status, outboundId);
        final List<LocationLPN> locationLPNList = List.of();
        final Location toteLocation = createToteLocation(
                locationLPNList,
                StorageType.CELL);

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토트가 아닙니다.");
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 로케이션에 LPN이 존재하는 경우 IllegalArgumentException이 발생한다.")
    void assignPickingTote_alreadyExistsLocationLPN() {
        final long outboundId = 1L;
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status, outboundId);
        final List<LocationLPN> locationLPNList = List.of(Instancio.create(LocationLPN.class));
        final Location toteLocation = createToteLocation(
                locationLPNList,
                StorageType.TOTE);

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품에 사용할 토트에 상품이 이미 담겨 있습니다.");
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 출고의 상태가 출고 대기가 아닌 경우 IllegalArgumentException이 발생한다.")
    void assignPickingTote_invalidStatus() {
        final long outboundId = 1L;
        final OutboundStatus status = OutboundStatus.PICKING;
        final Outbound outbound = createOutbound(status, outboundId);
        final List<LocationLPN> locationLPNList = List.of();
        final Location toteLocation = createToteLocation(
                locationLPNList,
                StorageType.TOTE);

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품할 토트 할당은 출고 대기상태에만 가능합니다.");
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 출고에 이미 토트가 배정되어 있는 경우 IllegalStateException이 발생한다.")
    void assignPickingTote_alreadyAssignedTote() {
        final long outboundId = 1L;
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status, outboundId);
        final List<LocationLPN> locationLPNList = List.of();
        final Location toteLocation = createToteLocation(
                locationLPNList,
                StorageType.TOTE);
        outbound.assignPickingTote(toteLocation);

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 할당된 토트가 존재합니다.");
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 포장재가 할당되지 않은 경우 IllegalStateException이 발생한다.")
    void assignPickingTote_unassignedPackagingMaterial() {
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status);
        final List<LocationLPN> locationLPNList = List.of();
        final Location toteLocation = createToteLocation(
                locationLPNList,
                StorageType.TOTE);

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("추천 포장재가 할당되지 않았습니다.");
    }

    private Outbound createOutbound(final OutboundStatus status) {
        return Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getOutboundStatus), () -> status)
                .supply(Select.field(Outbound::getCushioningMaterial), () -> CushioningMaterial.NONE)
                .supply(Select.field(Outbound::getCushioningMaterialQuantity), () -> 0)
                .ignore(Select.field(Outbound::getOutboundItems))
                .ignore(Select.field(Outbound::getToteLocation))
                .ignore(Select.field(Outbound::getRecommendedPackagingMaterial))
                .create();
    }

    @Test
    @DisplayName("출고의 상태가 집품 중인지 확인한다.")
    void isPickingProgress() {
        final OutboundStatus status = OutboundStatus.PICKING;
        final Outbound outbound = createOutbound(status);

        final boolean isPickingProgress = outbound.isPickingProgress();

        assertThat(isPickingProgress).isTrue();
    }

    @Test
    @DisplayName("출고의 상태가 집품 중인지 확인한다. - 출고의 상태가 집품 중이 아닌 경우 false를 반환한다.")
    void isPickingProgress_false() {
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status);

        final boolean isPickingProgress = outbound.isPickingProgress();

        assertThat(isPickingProgress).isFalse();
    }

    @Test
    @DisplayName("출고의 상태를 집품 대기 상태로 변경한다." +
            "집품 대기 상태가 되기 위해서는 출고의 상태가 대기여야 하고 토트가 할당되어 있어야 한다.")
    void startPickingReady() {
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status);
        final PackagingMaterial packagingMaterial = Instancio.create(PackagingMaterial.class);
        outbound.assignRecommendedPackagingMaterial(packagingMaterial);
        final Location toteLocation = createToteLocation(
                List.of(),
                StorageType.TOTE);
        outbound.assignPickingTote(toteLocation);

        outbound.startPickingReady();

        assertThat(outbound.isPickingReadyStatus()).isTrue();
    }

    @Test
    @DisplayName("출고의 상태를 집품 대기 상태로 변경한다. - 토트가 할당되어 있지 않은 경우 IllegalStateException이 발생한다.")
    void startPickingReady_fail_unassigned_tote() {
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status);

        assertThatThrownBy(() -> {
            outbound.startPickingReady();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품 대기 상태가 되기 위해서는 할당된 토트가 필요합니다.");
    }

    @Test
    @DisplayName("출고의 상태를 집품 대기 상태로 변경한다. - 출고의 상태가 출고 대기가 아닌 경우 IllegalStateException이 발생한다.")
    void startPickingReady_invalid_status() {
        final OutboundStatus status = OutboundStatus.PICKING;
        final Outbound outbound = createOutbound(status);

        assertThatThrownBy(() -> {
            outbound.startPickingReady();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품대기 상태가 되기 위해서는 출고 준비 상태여야 합니다. 현재 상태: 피킹 중");
    }


    @Test
    @DisplayName("집품의 상태를 집품 중으로 변경한다." +
            "집품중 상태가 되기 위해서는 출고의 상태가 집품 대기여야 하고 토트가 할당되어 있어야 한다.")
    void startPickingProgress() {
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status);
        final PackagingMaterial packagingMaterial = Instancio.create(PackagingMaterial.class);
        outbound.assignRecommendedPackagingMaterial(packagingMaterial);
        final Location toteLocation = createToteLocation(
                List.of(),
                StorageType.TOTE);
        outbound.assignPickingTote(toteLocation);
        outbound.startPickingReady();

        outbound.startPickingProgress();

        assertThat(outbound.isPickingProgress()).isTrue();
    }

    @Test
    @DisplayName("집품의 상태를 집품 중으로 변경한다. - 출고의 상태가 집품 대기가 아닌 경우 IllegalStateException이 발생한다.")
    void startPickingProgress_invalid_status() {
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status);

        assertThatThrownBy(() -> {
            outbound.startPickingProgress();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품 진행 상태가 되기 위해서는 집품 대기 상태여야 합니다. 현재 상태: 출고 대기");
    }
}