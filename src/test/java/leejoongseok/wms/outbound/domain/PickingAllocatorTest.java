package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.UsagePurpose;
import leejoongseok.wms.outbound.exception.NotEnoughInventoryException;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PickingAllocatorTest {

    @Test
    @DisplayName("출고에 집품목록을 할당한다.")
    void allocate() {
        final Outbound outbound = Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getOutboundItems), () -> List.of(
                        createOutboundItem(1L, 3),
                        createOutboundItem(2L, 2),
                        createOutboundItem(3L, 1)))
                .supply(Select.field(Outbound::getOutboundStatus), () -> OutboundStatus.PICKING_READY)
                .create();
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1L);
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(1L, 1L, "locationBarcode-1", 2, expirationAt),
                createLocationLPN(1L, 2L, "locationBarcode-2", 3, expirationAt),
                createLocationLPN(1L, 2L, "locationBarcode-3", 3, expirationAt),
                createLocationLPN(2L, 3L, "locationBarcode-3", 1, expirationAt),
                createLocationLPN(2L, 4L, "locationBarcode-4", 3, expirationAt),
                createLocationLPN(3L, 5L, "locationBarcode-5", 2, expirationAt)
        );

        PickingAllocator.allocate(outbound, locationLPNList);

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

    private OutboundItem createOutboundItem(
            final long itemId,
            final int outboundQuantity) {
        final Item item = createItem(itemId);
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundQuantity)
                .supply(Select.field(OutboundItem::getItem), () -> item)
                .ignore(Select.field(OutboundItem::getPickings))
                .create();
    }

    private Item createItem(final Long itemId) {
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getId), () -> itemId)
                .create();
    }

    private LocationLPN createLocationLPN(
            final Long itemId,
            final long locationLPNId,
            final String locationBarcode,
            final int inventoryQuantity,
            final LocalDateTime expirationAt) {
        final LPN lpn = Instancio.of(LPN.class)
                .supply(Select.field(LPN::getExpirationAt), () -> expirationAt)
                .supply(Select.field(LPN::getItemId), () -> itemId)
                .create();
        final Location location = Instancio.of(Location.class)
                .supply(Select.field(Location::getLocationBarcode), () -> locationBarcode)
                .supply(Select.field(Location::getUsagePurpose), () -> UsagePurpose.STOW)
                .create();
        return Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getId), () -> locationLPNId)
                .supply(Select.field(LocationLPN::getLpn), () -> lpn)
                .supply(Select.field(LocationLPN::getLocation), () -> location)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> inventoryQuantity)
                .supply(Select.field(LocationLPN::getItemId), () -> itemId)
                .create();
    }

    @Test
    @DisplayName("출고에 집품목록을 할당한다. - itemId1 재고 부족")
    void allocate_fail_inventory() {
        final Outbound outbound = Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getOutboundItems), () -> List.of(
                        createOutboundItem(1L, 3)))
                .supply(Select.field(Outbound::getOutboundStatus), () -> OutboundStatus.PICKING_READY)
                .create();
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1L);
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(1L, 1L, "locationBarcode-1", 2, expirationAt)
        );

        assertThatThrownBy(() -> {
            PickingAllocator.allocate(outbound, locationLPNList);
        }).isInstanceOf(NotEnoughInventoryException.class)
                .hasMessageContaining("집품할 상품의 재고가 부족합니다.상품ID: 1, 재고수량: 2, 필요한 수량: 3");
    }

    @Test
    @DisplayName("출고에 집품목록을 할당한다. - itemId1 재고 부족 (유통기한이 지남)")
    void allocate_fail_invalid_inventory() {
        final Outbound outbound = Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getOutboundItems), () -> List.of(
                        createOutboundItem(1L, 3)))
                .supply(Select.field(Outbound::getOutboundStatus), () -> OutboundStatus.PICKING_READY)
                .create();
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1L);
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(1L, 1L, "locationBarcode-1", 2, expirationAt),
                createLocationLPN(1L, 2L, "locationBarcode-2", 2, expirationAt.minusDays(3))
        );

        assertThatThrownBy(() -> {
            PickingAllocator.allocate(outbound, locationLPNList);
        }).isInstanceOf(NotEnoughInventoryException.class)
                .hasMessageContaining("집품할 상품의 재고가 부족합니다.상품ID: 1, 재고수량: 2, 필요한 수량: 3");
    }
}