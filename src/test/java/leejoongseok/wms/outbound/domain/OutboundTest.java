package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.common.fixture.ItemFixture;
import leejoongseok.wms.common.fixture.ItemSizeFixture;
import leejoongseok.wms.common.fixture.LocationFixture;
import leejoongseok.wms.common.fixture.LocationLPNFixture;
import leejoongseok.wms.common.fixture.OutboundFixture;
import leejoongseok.wms.common.fixture.OutboundItemFixture;
import leejoongseok.wms.common.fixture.PackagingMaterialFixture;
import leejoongseok.wms.common.fixture.PickingFixture;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.outbound.exception.OutboundItemIdNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboundTest {

    @Test
    @DisplayName("출고를 분할 한다.")
    void split() {
        final Outbound outbound = OutboundFixture.aOutboundWithNoCushionAndNoOutboundItemAndNoToteLocation()
                .withId(1L)
                .withOutboundStatus(OutboundStatus.READY)
                .build();
        outbound.addOutboundItem(OutboundItemFixture.aOutboundItem()
                .withId(1L)
                .withOutboundQuantity(2)
                .build());

        final Long outboundItemId = 1L;
        final Integer quantityToSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = new SplittableOutboundItem(
                outboundItemId,
                quantityToSplit);

        final Outbound splittedOutbound = outbound.split(
                List.of(splittableOutboundItem));

        assertThat(splittedOutbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(splittedOutbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("출고를 분할 한다. [출고 상태가 READY가 아닌 경우]")
    void fail_split_invalid_outbound_status() {
        final Outbound invalidStatusOutbound = OutboundFixture.aOutboundWithNoCushionAndNoOutboundItemAndNoToteLocation()
                .withId(1L)
                .withOutboundStatus(OutboundStatus.PICKING_IN_PROGRESS)
                .build();
        invalidStatusOutbound.addOutboundItem(OutboundItemFixture.aOutboundItem()
                .withId(1L)
                .withOutboundQuantity(2)
                .build());

        final Long outboundItemId = 1L;
        final Integer quantityToSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = new SplittableOutboundItem(
                outboundItemId,
                quantityToSplit);

        assertThatThrownBy(() -> {
            invalidStatusOutbound.split(List.of(splittableOutboundItem));
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("출고는 대기 상태에서만 분할 할 수 있습니다.");
    }

    @Test
    @DisplayName("출고를 분할 한다. [분할 하려는 출고 품목이 존재하지 않는 경우]")
    void fail_split_not_found_target_outbound_item_id() {
        final Outbound outbound = OutboundFixture.aOutboundWithNoCushionAndNoOutboundItemAndNoToteLocation()
                .withId(1L)
                .withOutboundStatus(OutboundStatus.READY)
                .build();
        outbound.addOutboundItem(OutboundItemFixture.aOutboundItem()
                .withId(1L)
                .withOutboundQuantity(2)
                .build());

        final Long outboundItemId = 2L;
        final Integer quantityToSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = new SplittableOutboundItem(
                outboundItemId,
                quantityToSplit);

        assertThatThrownBy(() -> {
            outbound.split(List.of(splittableOutboundItem));
        }).isInstanceOf(OutboundItemIdNotFoundException.class)
                .hasMessageContaining("출고 상품 ID [2]에 해당하는 출고 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("출고를 분할 한다. [분할 하려는 출고 상품의 수량이 출고 상품의 수량보다 많은 경우]")
    void fail_split_over_split_quantity_target_outbound_item_quantity() {
        final Outbound outbound = OutboundFixture.aOutboundWithNoCushionAndNoOutboundItemAndNoToteLocation()
                .withId(1L)
                .withOutboundStatus(OutboundStatus.READY)
                .build();
        outbound.addOutboundItem(OutboundItemFixture.aOutboundItem()
                .withId(1L)
                .withOutboundQuantity(2)
                .build());

        final Long outboundItemId = 1L;
        final Integer quantityToSplit = 2;
        final SplittableOutboundItem splittableOutboundItem = new SplittableOutboundItem(
                outboundItemId,
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
        final Outbound outbound = OutboundFixture.aOutboundWithNoCushionAndNoOutboundItemAndNoToteLocation()
                .withId(1L)
                .withOutboundStatus(OutboundStatus.READY)
                .build();
        outbound.addOutboundItem(OutboundItemFixture.aOutboundItem()
                .withId(1L)
                .withOutboundQuantity(2)
                .build());
        outbound.addOutboundItem(OutboundItemFixture.aOutboundItem()
                .withId(2L)
                .withOutboundQuantity(2)
                .build());

        final Long outboundItemId = 1L;
        final Integer quantityToSplit = 2;
        final SplittableOutboundItem splittableOutboundItem = new SplittableOutboundItem(
                outboundItemId,
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
        final Outbound outbound = OutboundFixture.aOutboundWithNoCushionAndNoOutboundItemAndNoToteLocation()
                .withId(1L)
                .withOutboundStatus(OutboundStatus.READY)
                .build();
        outbound.addOutboundItem(OutboundItemFixture.aOutboundItem()
                .withId(1L)
                .withOutboundQuantity(2)
                .build());
        outbound.addOutboundItem(OutboundItemFixture.aOutboundItem()
                .withId(2L)
                .withOutboundQuantity(2)
                .build());

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityToSplit = 1;
        final SplittableOutboundItem splittableOutboundItem = new SplittableOutboundItem(
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
        final Outbound outbound = OutboundFixture.aOutboundWithNoRecommendedPackagingMaterial()
                .withCushioningMaterial(CushioningMaterial.BUBBLE_WRAP)
                .withCushioningMaterialQuantity(1)
                .withOutboundItems(List.of(OutboundItemFixture.aOutboundItem()
                        .withItem(ItemFixture.aItem()
                                .withItemSize(ItemSizeFixture.aItemSize()
                                        .withWidthInMillimeter(100)
                                        .withHeightInMillimeter(100)
                                        .withLengthInMillimeter(100)
                                        .build())
                                .build())
                        .withOutboundQuantity(1)
                        .build()))
                .build();

        final Long totalVolume = outbound.calculateTotalVolume();

        final int cushioningMaterialVolume = 1000;
        final int itemTotalVolume = 1000000;
        final int outboundTotalVolume = itemTotalVolume + cushioningMaterialVolume;
        assertThat(totalVolume).isEqualTo(outboundTotalVolume);

    }

    @Test
    @DisplayName("출고의 총 무게를 계산한다. (출고의 무게 = 상품의 무게 * 상품의 수량 + 완충재의 무게 * 완충재의 수량)")
    void calculateTotalWeightInGrams() {
        final Outbound outbound = OutboundFixture.aOutboundWithNoRecommendedPackagingMaterial()
                .withCushioningMaterial(CushioningMaterial.BUBBLE_WRAP)
                .withCushioningMaterialQuantity(1)
                .withOutboundItems(List.of(OutboundItemFixture.aOutboundItem()
                        .withItem(ItemFixture.aItem()
                                .withWeightInGrams(100)
                                .build())
                        .withOutboundQuantity(1)
                        .build()))
                .build();

        final Long totalWeightInGrams = outbound.calculateTotalWeightInGrams();

        final int cushioningMaterialWeightInGrams = 10;
        final int itemTotalWeightInGrams = 100;
        final int outboundTotalWeightInGrams =
                itemTotalWeightInGrams + cushioningMaterialWeightInGrams;
        assertThat(totalWeightInGrams).isEqualTo(outboundTotalWeightInGrams);
    }

    @Test
    @DisplayName("출고의 상태가 출고 대기 인지 확인한다.")
    void isReadyStatus() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady()
                .build();

        final boolean isReadyStatus = outbound.isReadyStatus();

        assertThat(isReadyStatus).isTrue();
    }

    @Test
    @DisplayName("출고의 상태가 출고 대기 인지 확인한다. - 출고 대기가 아닌 경우 isReadyStatus는 false를 반환한다.")
    void isNotReadyStatus() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingInProgress()
                .build();

        final boolean isReadyStatus = outbound.isReadyStatus();

        assertThat(isReadyStatus).isFalse();
    }

    @Test
    @DisplayName("출고에 토트가 배정되어 있는 상태인지 확인한다.")
    void hasAssignedTote() {
        final Outbound outbound = OutboundFixture.aOutbound()
                .withToteLocation(LocationFixture.aLocationWithTote().build())
                .build();

        final boolean hasAssignedTote = outbound.hasAssignedTote();

        assertThat(hasAssignedTote).isTrue();
    }

    @Test
    @DisplayName("출고에 토트가 배정되어 있는 상태인지 확인한다. - 토트가 배정되어 있지 않은 경우 hasAssignedTote는 false를 반환한다.")
    void hasAssignedTote_false() {
        final Outbound outbound = OutboundFixture.aOutbound()
                .withToteLocation(null)
                .build();

        final boolean hasAssignedTote = outbound.hasAssignedTote();

        assertThat(hasAssignedTote).isFalse();
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다.")
    void assignPickingTote() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady()
                .ignoreToteLocation()
                .build();
        final Location toteLocation = LocationFixture.aLocationWithNoLocationLPNList()
                .withStorageType(StorageType.TOTE)
                .build();

        outbound.assignPickingTote(toteLocation);

        assertThat(outbound.hasAssignedTote()).isTrue();
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 로케이션이 토트가 아닌 경우 IllegalArgumentException이 발생한다.")
    void assignPickingTote_isNotTote() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady()
                .ignoreToteLocation()
                .build();
        final Location cellLocation = LocationFixture.aLocationWithNoLocationLPNList()
                .withStorageType(StorageType.CELL)
                .build();

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(cellLocation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토트가 아닙니다.");
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 로케이션에 LPN이 존재하는 경우 IllegalArgumentException이 발생한다.")
    void assignPickingTote_alreadyExistsLocationLPN() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady()
                .ignoreToteLocation()
                .build();
        final Location toteLocation = LocationFixture.aLocationWithTote()
                .withLocationLPNList(List.of(Instancio.create(LocationLPN.class)))
                .build();

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품에 사용할 토트에 상품이 이미 담겨 있습니다.");
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 출고의 상태가 출고 대기가 아닌 경우 IllegalArgumentException이 발생한다.")
    void assignPickingTote_invalidStatus() {
        final Outbound invalidStatusOutbound = OutboundFixture.aOutboundWithPickingInProgress()
                .ignoreToteLocation()
                .build();
        final Location toteLocation = LocationFixture.aLocationWithNoLocationLPNList()
                .withStorageType(StorageType.TOTE)
                .build();

        assertThatThrownBy(() -> {
            invalidStatusOutbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품할 토트 할당은 출고 대기상태에만 가능합니다.");
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 출고에 이미 토트가 배정되어 있는 경우 IllegalStateException이 발생한다.")
    void assignPickingTote_alreadyAssignedTote() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady()
                .build();
        final Location toteLocation = LocationFixture.aLocationWithNoLocationLPNList()
                .withStorageType(StorageType.TOTE)
                .build();

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 할당된 토트가 존재합니다.");
    }

    @Test
    @DisplayName("출고에 집품할 토트를 배정한다. - 포장재가 할당되지 않은 경우 IllegalStateException이 발생한다.")
    void assignPickingTote_unassignedPackagingMaterial() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady()
                .ignoreRecommendedPackagingMaterial()
                .ignoreToteLocation()
                .build();
        final Location toteLocation = LocationFixture.aLocationWithNoLocationLPNList()
                .withStorageType(StorageType.TOTE)
                .build();

        assertThatThrownBy(() -> {
            outbound.assignPickingTote(toteLocation);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("추천 포장재가 할당되지 않았습니다.");
    }

    @Test
    @DisplayName("출고의 상태가 집품 중인지 확인한다.")
    void isPickingProgress() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingInProgress().build();

        final boolean isPickingProgress = outbound.isPickingInProgress();

        assertThat(isPickingProgress).isTrue();
    }

    @Test
    @DisplayName("출고의 상태가 집품 중인지 확인한다. - 출고의 상태가 집품 중이 아닌 경우 false를 반환한다.")
    void isPickingProgress_false() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady().build();

        final boolean isPickingProgress = outbound.isPickingInProgress();

        assertThat(isPickingProgress).isFalse();
    }

    @Test
    @DisplayName("출고의 상태를 집품 대기 상태로 변경한다." +
            "집품 대기 상태가 되기 위해서는 출고의 상태가 대기여야 하고 토트가 할당되어 있어야 한다.")
    void startPickingReady() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady()
                .withRecommendedPackagingMaterial(PackagingMaterialFixture.aPackagingMaterial().build())
                .withToteLocation(LocationFixture.aLocationWithTote().build())
                .build();

        outbound.startPickingReady();

        assertThat(outbound.isPickingReadyStatus()).isTrue();
    }

    @Test
    @DisplayName("출고의 상태를 집품 대기 상태로 변경한다. - 토트가 할당되어 있지 않은 경우 IllegalStateException이 발생한다.")
    void startPickingReady_fail_unassigned_tote() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady()
                .ignoreToteLocation()
                .build();

        assertThatThrownBy(() -> {
            outbound.startPickingReady();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품 대기 상태가 되기 위해서는 할당된 토트가 필요합니다.");
    }

    @Test
    @DisplayName("출고의 상태를 집품 대기 상태로 변경한다. - 출고의 상태가 출고 대기가 아닌 경우 IllegalStateException이 발생한다.")
    void startPickingReady_invalid_status() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingInProgress().build();

        assertThatThrownBy(() -> {
            outbound.startPickingReady();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품대기 상태가 되기 위해서는 출고 준비 상태여야 합니다. 현재 상태: 피킹 중");
    }


    @Test
    @DisplayName("집품의 상태를 집품 중으로 변경한다." +
            "집품중 상태가 되기 위해서는 출고의 상태가 집품 대기여야 하고 토트가 할당되어 있어야 한다.")
    void startPickingProgress() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus()
                .withToteLocation(LocationFixture.aLocationWithTote()
                        .build())
                .withRecommendedPackagingMaterial(PackagingMaterialFixture.aPackagingMaterial().build())
                .build();

        outbound.startPickingProgress();

        assertThat(outbound.isPickingInProgress()).isTrue();
    }

    @Test
    @DisplayName("집품의 상태를 집품 중으로 변경한다. - 출고의 상태가 집품 대기가 아닌 경우 IllegalStateException이 발생한다.")
    void startPickingProgress_invalid_status() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady().build();

        assertThatThrownBy(() -> {
            outbound.startPickingProgress();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품 진행 상태가 되기 위해서는 집품 대기 상태여야 합니다. 현재 상태: 출고 대기");
    }

    @Test
    @DisplayName("집품에 할당된 출고 상품의 수량만큼 재고를 차감한다.")
    void deductAllocatedInventory() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus()
                .withRecommendedPackagingMaterial(PackagingMaterialFixture.aPackagingMaterial().build())
                .withToteLocation(LocationFixture.aLocationWithNoLocationLPNList().build())
                .withOutboundItems(List.of(OutboundItemFixture.aOutboundItem()
                        .withPickings(List.of(PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                                .withQuantityRequiredForPick(5)
                                .withLocationLPN(locationLPN)
                                .build()))
                        .build()))
                .build();

        outbound.deductAllocatedInventory();

        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(10 - 5);
    }

    @Test
    @DisplayName("집품에 할당된 출고 상품의 수량만큼 재고를 차감한다. - 집품의 상태가 집품 대기가 아님")
    void deductAllocatedInventory_invalidStatus() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingInProgress().build();

        assertThatThrownBy(() -> {
            outbound.deductAllocatedInventory();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Picking에 할당된 집품 수량만큼 LocationLPN의 재고 수량을 감소시키기 위해서는" +
                        " 집품 대기 상태여야 하고 토트에 집품한 상품이 없어야합니다. 현재 상태: 피킹 중");
    }


    @Test
    @DisplayName("출고의 상태를 집품완료로 변경한다.")
    void completePicking() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingInProgress()
                .withOutboundItems(List.of(OutboundItemFixture.aOutboundItem()
                        .withPickings(List.of(PickingFixture.aPickingWithCompletedPickingNoPickedQuantity().build()))
                        .build()))
                .build();

        outbound.completePicking();

        assertThat(outbound.isCompletedPicking()).isTrue();
    }

    @Test
    @DisplayName("출고의 상태를 집품완료로 변경한다. - 집품 진행상태가 아닌경우 짐품 완료할 수 없음.")
    void completePicking_invalid_status() {
        final Outbound invalidStatusOutbound = OutboundFixture.aOutboundWithPickingReadyStatus().build();

        assertThatThrownBy(() -> {
            invalidStatusOutbound.completePicking();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품 완료 처리를 위해서는 집품 진행 상태여야 합니다. 현재 상태: 피킹 대기(토트만 할당된 상태)");
    }

    @Test
    @DisplayName("출고의 상태를 집품완료로 변경한다. - 출고 상품별 집품이 완료되지 않은 경우 집품 완료할 수 없음.")
    void completePicking_not_completed_picked() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingInProgress()
                .withOutboundItems(List.of(OutboundItemFixture.aOutboundItem()
                        .withPickings(List.of(PickingFixture.aPickingWithInProgressPickingNoPickedQuantity().build()))
                        .build()))
                .build();

        assertThatThrownBy(() -> {
            outbound.completePicking();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("집품 완료 처리를 위해서는 모든 상품의 집품이 완료되어야 합니다.");
    }

    @Test
    @DisplayName("발행한 송장의 추적번호를 할당한다.")
    void assignTrackingNumber() {
        final Outbound outbound = OutboundFixture.aOutboundWithNoTrackingNumber().build();

        outbound.assignTrackingNumber("trackingNumber");

        assertThat(outbound.hasTrackingNumber()).isTrue();
        assertThat(outbound.getTrackingNumber()).isEqualTo("trackingNumber");
    }

    @Test
    @DisplayName("발행한 송장의 추적번호를 할당한다. - 이미 할당된 추적번호가 있을경우 예외 발생")
    void assignTrackingNumber_exists_tracking_number() {
        final Outbound outbound = OutboundFixture.aOutbound().build();

        assertThatThrownBy(() -> {
            outbound.assignTrackingNumber("trackingNumber");
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 할당된 송장번호가 존재합니다.");
    }

    @Test
    @DisplayName("포장 정보를 등록한다.")
    void assignPacking() {
        final Outbound outbound = OutboundFixture.aOutboundWithNoCushion()
                .withOutboundStatus(OutboundStatus.PICKING_COMPLETED)
                .withOutboundItems(List.of(
                        OutboundItemFixture.aOutboundItem()
                                .withPickings(List.of(PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                                        .withQuantityRequiredForPick(5)
                                        .withLocationLPN(LocationLPNFixture.aLocationLPN()
                                                .withInventoryQuantity(10)
                                                .build())
                                        .build()))
                                .withItem(ItemFixture.aItem()
                                        .withWeightInGrams(100)
                                        .build())
                                .withOutboundQuantity(5)
                                .build()))
                .ignoreRecommendedPackagingMaterial()
                .build();
        final PackagingMaterial packagingMaterial = PackagingMaterialFixture.aPackagingMaterial()
                .withMaxWeightInGrams(100)
                .build();
        final int packagingWeightInGrams = 600;

        outbound.assignPacking(packagingMaterial, packagingWeightInGrams);

        assertThat(outbound.isPackingInProgress()).isTrue();
    }

    @Test
    @DisplayName("포장 정보를 등록한다. - 집품이 완료되지 않은 경우 예외 발생")
    void assignPacking_invalid_status() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingInProgress()
                .build();
        final PackagingMaterial packagingMaterial = PackagingMaterialFixture.aPackagingMaterial().build();

        final int packagingWeightInGrams = 600;
        assertThatThrownBy(() -> {
            outbound.assignPacking(packagingMaterial, packagingWeightInGrams);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("포장을 위해서는 집품이 완료되어야 합니다.");
    }

    @Test
    @DisplayName("포장 정보를 등록한다. - 무게가 100g 이상 차이날경우 예외 발생")
    void assignPacking_diff_weight() {
        final Outbound outbound = OutboundFixture.aOutboundWithNoCushion()
                .withOutboundStatus(OutboundStatus.PICKING_COMPLETED)
                .withOutboundItems(List.of(
                        OutboundItemFixture.aOutboundItem()
                                .withPickings(List.of(PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                                        .withQuantityRequiredForPick(5)
                                        .withLocationLPN(LocationLPNFixture.aLocationLPN()
                                                .withInventoryQuantity(10)
                                                .build())
                                        .build()))
                                .withItem(ItemFixture.aItem()
                                        .withWeightInGrams(100)
                                        .build())
                                .withOutboundQuantity(5)
                                .build()))
                .ignoreRecommendedPackagingMaterial()
                .build();
        final PackagingMaterial packagingMaterial = PackagingMaterialFixture.aPackagingMaterial()
                .withMaxWeightInGrams(100)
                .build();
        final int packagingWeightInGrams = 1000;

        assertThatThrownBy(() -> {
            outbound.assignPacking(packagingMaterial, packagingWeightInGrams);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("실중량과 상품의 포장예상 총중량의 차이가 100g 이상입니다. 실중량: 1000, 상품의 포장예상 총중량: 500");
    }

    @Test
    @DisplayName("포장을 완료한다.")
    void completePacking() {
        final Outbound outbound = OutboundFixture.withPackingInProgress().build();

        outbound.completePacking();

        assertThat(outbound.isCompletedPacking()).isTrue();
    }

    @Test
    @DisplayName("포장을 완료한다. - 송장이 발행되지 않은 경우 예외 발생")
    void completePacking_unissued_waybill() {
        final Outbound outbound = OutboundFixture.withPackingInProgress()
                .ignoreTrackingNumber()
                .build();

        assertThatThrownBy(() -> {
            outbound.completePacking();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("포장을 완료하기 위해서는 송장이 발행되어야합니다.");
    }

    @Test
    @DisplayName("포장을 완료한다. - 포장을 완료할 수 없는 상태 (포장 중이어야함.)")
    void completePacking_invalid_status() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady().build();

        assertThatThrownBy(() -> {
            outbound.completePacking();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("포장 완료 처리를 위해서는 포장 진행 상태여야 합니다.");
    }

    @Test
    @DisplayName("집품 완료한 출고건의 검수를 통과한다.")
    void passInspection() {
        final Outbound outbound = OutboundFixture.withCompletedPicking().build();

        outbound.passInspection();

        assertThat(outbound.isPassedInspection()).isTrue();

    }

    @Test
    @DisplayName("집품 완료한 출고건의 검수를 통과한다. - 검수를 통과할 수 없는 상태 (집품 완료 상태여야함.)")
    void passInspection_invalid_status() {
        final Outbound outbound = OutboundFixture.withPackingInProgress().build();

        assertThatThrownBy(() -> {
            outbound.passInspection();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("검수 통과 처리를 위해서는 집품이 완료되어야 합니다.");
    }

    @Test
    @DisplayName("집품 완료한 출고건의 검수를 불통과한다.")
    void failInspection() {
        final Outbound outbound = OutboundFixture.withCompletedPicking().build();
        final String 품질불량 = "품질불량";

        outbound.failInspection(품질불량);

        assertThat(outbound.isStopped()).isTrue();
        assertThat(outbound.getStoppedReason()).isEqualTo("품질불량");
    }

    @Test
    @DisplayName("집품 완료한 출고건의 검수를 불통과한다. - 검수를 불통과할 수 없는 상태 (집품 완료 상태여야함.)")
    void failInspection_invalid_status() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus().build();
        final String 품질불량 = "품질불량";

        assertThatThrownBy(() -> {
            outbound.failInspection(품질불량);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("검수 불합격 처리를 위해서는 집품이 완료되어야 합니다");
    }

    @Test
    @DisplayName("출고를 중지한다.")
    void stop() {
        final Outbound outbound = OutboundFixture.aOutbound().build();

        outbound.stop("포장 파손");

        assertThat(outbound.isStopped()).isTrue();
    }

    @Test
    @DisplayName("출고를 중지한다. - 중지 사유가 없는 경우 예외 발생")
    void stop_empty_reason() {
        final Outbound outbound = OutboundFixture.aOutbound().build();
        final String emptyReason = "";

        assertThatThrownBy(() -> {
            outbound.stop(emptyReason);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중지 사유는 필수입니다.");
    }

    @Test
    @DisplayName("출고를 초기화한다.")
    void reset() {
        final Outbound outbound = OutboundFixture.aOutboundWithStopped().build();

        outbound.reset();

        assertThat(outbound.isReadyStatus()).isTrue();
    }

    @Test
    @DisplayName("출고를 초기화한다. - 출고건이 중지 상태가 아닌 경우 예외 발생")
    void reset_invalid_status() {
        final Outbound outbound = OutboundFixture.aOutboundWithReady().build();

        assertThatThrownBy(() -> {
            outbound.reset();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중지된 출고만 초기화할 수 있습니다.");
    }
}