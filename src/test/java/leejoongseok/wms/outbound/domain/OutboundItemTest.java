package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.common.fixture.ItemFixture;
import leejoongseok.wms.common.fixture.ItemSizeFixture;
import leejoongseok.wms.common.fixture.LocationLPNFixture;
import leejoongseok.wms.common.fixture.OutboundItemFixture;
import leejoongseok.wms.common.fixture.PickingFixture;
import leejoongseok.wms.location.domain.LocationLPN;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboundItemTest {

    @Test
    @DisplayName("출고 상품을 분할 한다.")
    void split() {
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withOutboundQuantity(1)
                .build();

        final int quantityToSplit = 1;
        final OutboundItem splittedOutboundItem = outboundItem.split(quantityToSplit);
        assertThat(splittedOutboundItem.getOutboundQuantity()).isEqualTo(1);
        assertThat(outboundItem.getItem()).isEqualTo(splittedOutboundItem.getItem());
        assertThat(outboundItem.getUnitPrice()).isEqualTo(splittedOutboundItem.getUnitPrice());
    }

    @Test
    @DisplayName("출고 상품을 분할 한다.[ 실패 : 분할 수량이 출고 수량보다 많을 경우 ]")
    void fail_split_invalid_quantity_of_split() {
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withOutboundQuantity(1)
                .build();

        final int overQuantityToSplit = 2;
        assertThatThrownBy(() -> {
            outboundItem.split(overQuantityToSplit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("분할 수량은 출고 수량보다 작거나 같아야 합니다. 출고 수량: 1, 분할 수량: 2");
    }

    @Test
    @DisplayName("출고 상품을 분할 한다.[ 실패 : 분할 수량 0 이하 ]")
    void fail_split_zero_quantity_of_split() {
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withOutboundQuantity(1)
                .build();

        final int zeroQuantityToSplit = 0;
        assertThatThrownBy(() -> {
            outboundItem.split(zeroQuantityToSplit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출고 수량은 0보다 커야합니다.");
    }

    @Test
    @DisplayName("출고 상품의 부피를 계산한다. (출고 상품의 부피 = 상품의 부피 * 출고 수량)")
    void calculateVolume() {
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withItem(ItemFixture.aItem()
                        .withItemSize(ItemSizeFixture.aItemSize()
                                .withWidthInMillimeter(100)
                                .withLengthInMillimeter(100)
                                .withHeightInMillimeter(100)
                                .build())
                        .build())
                .withOutboundQuantity(1)
                .build();

        final Long outboundItemVolume = outboundItem.calculateVolume();

        final Long itemVolume = 100L * 100L * 100L;
        final Long totalVolume = itemVolume * (Integer) 1;
        assertThat(outboundItemVolume).isEqualTo(totalVolume);
    }

    @Test
    @DisplayName("출고 상품의 무게를 계산한다. (출고 상품의 무게 = 상품의 무게 * 출고 수량)")
    void calculateWeightInGrams() {
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withOutboundQuantity(2)
                .withItem(ItemFixture.aItem()
                        .withWeightInGrams(100)
                        .build())
                .build();

        final Long outboundItemWeightInGrams = outboundItem.calculateWeightInGrams();

        assertThat(outboundItemWeightInGrams).isEqualTo(200L);
    }

    @Test
    @DisplayName("입력한 수량만큼 출고수량이 감소한다.")
    void decreaseQuantity() {
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withOutboundQuantity(2)
                .build();
        final Integer decreaseQuantity = 1;

        outboundItem.decreaseQuantity(decreaseQuantity);

        assertThat(outboundItem.getOutboundQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("입력한 수량만큼 출고수량이 감소한다. 감소할 수량이 현재 수량보다 많을 경우 예외가 발생한다.")
    void decreaseQuantity_fail_over_decrease_quantity() {
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withOutboundQuantity(2)
                .build();
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
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withOutboundQuantity(2)
                .build();
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
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withPickings(List.of(
                        PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                                .withQuantityRequiredForPick(5)
                                .withLocationLPN(locationLPN)
                                .build()))
                .build();

        outboundItem.deductAllocatedInventory();

        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(5);
    }


    @Test
    @DisplayName("출고 상품에 할당된 집품 목록만큼 LocationLPN의 재고를 차감한다. - pickings가 비어있을 경우 예외가 발생한다.")
    void deductAllocatedInventory_empty_pickings() {
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withPickings(List.of())
                .build();

        assertThatThrownBy(() -> {
            outboundItem.deductAllocatedInventory();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("집품 목록이 비어있습니다.");

    }

    @Test
    @DisplayName("출고 상품에 할당된 집품 목록만큼 LocationLPN의 재고를 차감한다. - 차감해야할 수량이 재고보다 많을 경우 예외가 발생한다.")
    void deductAllocatedInventory_over_deduct() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withPickings(List.of(
                        PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                                .withQuantityRequiredForPick(50)
                                .withLocationLPN(locationLPN)
                                .build()))
                .build();

        assertThatThrownBy(() -> {
            outboundItem.deductAllocatedInventory();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("차감할 재고 수량이 재고 수량보다 많습니다. 재고 수량: 10, 차감할 재고 수량: 50");
    }

    @Test
    @DisplayName("출고상품에 집품이 모두 완료되었는지 확인한다.")
    void isCompletedPicking() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(5)
                .withLocationLPN(locationLPN)
                .build();
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withPickings(List.of(picking))
                .build();
        picking.addManualPickedQuantity(locationLPN, 5);

        final boolean isCompletedPicking = outboundItem.isCompletedPicking();

        assertThat(isCompletedPicking).isTrue();
    }

    @Test
    @DisplayName("출고상품에 집품이 모두 완료되었는지 확인한다. - 집품이 완료되지 않았을 경우")
    void isCompletedPicking_not_completed() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final Picking picking = PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(5)
                .withLocationLPN(locationLPN)
                .build();
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withPickings(List.of(picking))
                .build();
        picking.addManualPickedQuantity(locationLPN, 4);

        final boolean isCompletedPicking = outboundItem.isCompletedPicking();

        assertThat(isCompletedPicking).isFalse();
    }

    @Test
    @DisplayName("출고상품에 할당된 집품목록을 전부 제거한다.")
    void resetPickings() {
        final List<Picking> pickings = new ArrayList<>();
        pickings.add(PickingFixture.aPickingWithReadyPickingNoPickedQuantity()
                .withQuantityRequiredForPick(5)
                .withLocationLPN(LocationLPNFixture.aLocationLPN()
                        .withInventoryQuantity(10)
                        .build())
                .build());
        final OutboundItem outboundItem = OutboundItemFixture.aOutboundItem()
                .withPickings(pickings)
                .build();
        assertThat(outboundItem.getPickings()).hasSize(1);

        outboundItem.resetPickings();

        assertThat(outboundItem.getPickings()).isEmpty();
    }
}