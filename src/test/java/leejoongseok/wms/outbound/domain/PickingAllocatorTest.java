package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.common.fixture.ItemFixture;
import leejoongseok.wms.common.fixture.LPNFixture;
import leejoongseok.wms.common.fixture.LocationFixture;
import leejoongseok.wms.common.fixture.LocationLPNFixture;
import leejoongseok.wms.common.fixture.OutboundFixture;
import leejoongseok.wms.common.fixture.OutboundItemFixture;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNList;
import leejoongseok.wms.outbound.exception.NotEnoughInventoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PickingAllocatorTest {

    private static Location createStowWith(final String locationBarcode) {
        return LocationFixture.aLocationWithStow()
                .withLocationBarcode(locationBarcode)
                .build();
    }

    private static LPN createLPNWithItemIdAndExpirationAt(final long itemId, final LocalDateTime expirationAt) {
        return LPNFixture.aLPN()
                .withItemId(itemId)
                .withExpirationAt(expirationAt)
                .build();
    }

    @Test
    @DisplayName("출고에 집품목록을 할당한다.")
    void allocate() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus()
                .withOutboundItems(List.of(
                        OutboundItemFixture.aOutboundItemWithNoPickings()
                                .withOutboundQuantity(3)
                                .withItem(ItemFixture.aItem()
                                        .withId(1L)
                                        .build())
                                .build(),
                        OutboundItemFixture.aOutboundItemWithNoPickings()
                                .withOutboundQuantity(2)
                                .withItem(ItemFixture.aItem()
                                        .withId(2L)
                                        .build())
                                .build(),
                        OutboundItemFixture.aOutboundItemWithNoPickings()
                                .withOutboundQuantity(1)
                                .withItem(ItemFixture.aItem()
                                        .withId(3L)
                                        .build())
                                .build()))
                .build();
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1L);
        final List<LocationLPN> locationLPNList = List.of(
                LocationLPNFixture.aLocationLPN()
                        .withId(1L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(1L, expirationAt))
                        .withLocation(createStowWith("locationBarcode-1"))
                        .withInventoryQuantity(2)
                        .withItemId(1L)
                        .build(),
                LocationLPNFixture.aLocationLPN()
                        .withId(2L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(1L, expirationAt))
                        .withLocation(createStowWith("locationBarcode-2"))
                        .withInventoryQuantity(3)
                        .withItemId(1L)
                        .build(),
                LocationLPNFixture.aLocationLPN()
                        .withId(3L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(1L, expirationAt))
                        .withLocation(createStowWith("locationBarcode-3"))
                        .withInventoryQuantity(3)
                        .withItemId(1L)
                        .build(),
                LocationLPNFixture.aLocationLPN()
                        .withId(4L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(2L, expirationAt))
                        .withLocation(createStowWith("locationBarcode-3"))
                        .withInventoryQuantity(1)
                        .withItemId(2L)
                        .build(),
                LocationLPNFixture.aLocationLPN()
                        .withId(5L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(2L, expirationAt))
                        .withLocation(createStowWith("locationBarcode-4"))
                        .withInventoryQuantity(3)
                        .withItemId(2L)
                        .build(),
                LocationLPNFixture.aLocationLPN()
                        .withId(6L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(3L, expirationAt))
                        .withLocation(createStowWith("locationBarcode-5"))
                        .withInventoryQuantity(2)
                        .withItemId(3L)
                        .build()
        );

        PickingAllocator.allocate(outbound, new LocationLPNList(locationLPNList));

        /**
         * 아래와 같은 순서로 집품에 LocationLPN이 할당된다.
         * 1. 유통기한이 가장 짧은 순서 (선입선출)
         * 2. 재고수량이 많은 순서 (그래야 최대한 적은 로케이션 LPN으로 집품이 가능하니까)
         * 3. 로케이션 바코드 명으로 정렬 (보통 로케이션 바코드는 A -> Z 순서로 정렬되어 있음)
         */
        final OutboundItem itemId1_OutboundItem = outbound.getOutboundItems().get(0);
        assertThat(itemId1_OutboundItem.getPickings()).hasSize(1);
        assertThat(itemId1_OutboundItem.getPickings().get(0).getLocationLPN().getLocationBarcode()).isEqualTo("locationBarcode-2");
        final OutboundItem itemId2_OutboundItem = outbound.getOutboundItems().get(1);
        assertThat(itemId2_OutboundItem.getPickings()).hasSize(1);
        assertThat(itemId2_OutboundItem.getPickings().get(0).getLocationLPN().getLocationBarcode()).isEqualTo("locationBarcode-4");
        final OutboundItem itemId3_OutboundItem = outbound.getOutboundItems().get(2);
        assertThat(itemId3_OutboundItem.getPickings()).hasSize(1);
        assertThat(itemId3_OutboundItem.getPickings().get(0).getLocationLPN().getLocationBarcode()).isEqualTo("locationBarcode-5");
    }

    @Test
    @DisplayName("출고에 집품목록을 할당한다. - itemId1 재고 부족")
    void allocate_fail_inventory() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus()
                .withOutboundItems(List.of(
                        OutboundItemFixture.aOutboundItemWithNoPickings()
                                .withOutboundQuantity(3)
                                .withItem(ItemFixture.aItem()
                                        .withId(1L)
                                        .build())
                                .build()))
                .build();
        final List<LocationLPN> locationLPNList = List.of(
                LocationLPNFixture.aLocationLPN()
                        .withId(1L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(1L, LocalDateTime.now().plusDays(1L)))
                        .withLocation(createStowWith("locationBarcode-1"))
                        .withInventoryQuantity(2)
                        .withItemId(1L)
                        .build()
        );

        assertThatThrownBy(() -> {
            PickingAllocator.allocate(outbound, new LocationLPNList(locationLPNList));
        }).isInstanceOf(NotEnoughInventoryException.class)
                .hasMessageContaining("집품할 상품의 재고가 부족합니다.상품ID: 1, 재고수량: 2, 필요한 수량: 3");
    }

    @Test
    @DisplayName("출고에 집품목록을 할당한다. - itemId1 재고 부족 (유통기한이 지남)")
    void allocate_fail_invalid_inventory() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus()
                .withOutboundItems(List.of(
                        OutboundItemFixture.aOutboundItemWithNoPickings()
                                .withOutboundQuantity(3)
                                .withItem(ItemFixture.aItem()
                                        .withId(1L)
                                        .build())
                                .build()))
                .build();
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1L);
        final List<LocationLPN> locationLPNList = List.of(
                LocationLPNFixture.aLocationLPN()
                        .withId(1L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(1L, expirationAt))
                        .withLocation(createStowWith("locationBarcode-1"))
                        .withInventoryQuantity(2)
                        .withItemId(1L)
                        .build(),
                LocationLPNFixture.aLocationLPN()
                        .withId(2L)
                        .withLPN(createLPNWithItemIdAndExpirationAt(1L, expirationAt.minusDays(3)))
                        .withLocation(createStowWith("locationBarcode-2"))
                        .withInventoryQuantity(2)
                        .withItemId(1L)
                        .build()
        );

        assertThatThrownBy(() -> {
            PickingAllocator.allocate(outbound, new LocationLPNList(locationLPNList));
        }).isInstanceOf(NotEnoughInventoryException.class)
                .hasMessageContaining("집품할 상품의 재고가 부족합니다.상품ID: 1, 재고수량: 2, 필요한 수량: 3");
    }
}