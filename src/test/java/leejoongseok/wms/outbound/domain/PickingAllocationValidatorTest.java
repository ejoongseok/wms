package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import leejoongseok.wms.outbound.exception.NotEnoughInventoryException;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PickingAllocationValidatorTest {

    @Test
    @DisplayName("출고하려는 주문이 집품 가능한지 확인.")
    void validate() {
        final long itemId = 1L;
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1);
        final int inventoryQuantity = 1;
        final LocationLPN locationLPN = createLocationLPN(
                itemId,
                expirationAt,
                inventoryQuantity);

        final int outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(
                itemId,
                outboundQuantity);

        PickingAllocationValidator.validate(
                createPickingReadyStatusOutbound(outboundItem),
                List.of(locationLPN));
    }

    private Outbound createPickingReadyStatusOutbound(final OutboundItem outboundItem) {
        final OutboundStatus status = OutboundStatus.READY;
        final Outbound outbound = createOutbound(status);
        final PackagingMaterial packagingMaterial = Instancio.create(PackagingMaterial.class);
        outbound.assignRecommendedPackagingMaterial(packagingMaterial);
        final Location toteLocation = createToteLocation(
                List.of(),
                StorageType.TOTE);
        outbound.assignPickingTote(toteLocation);
        outbound.startPickingReady();
        outbound.addOutboundItem(outboundItem);
        return outbound;
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

    private Location createToteLocation(
            final List<LocationLPN> locationLPNList,
            final StorageType storageType) {
        return Instancio.of(Location.class)
                .supply(Select.field(Location::getStorageType), () -> storageType)
                .supply(Select.field(Location::getLocationLPNList), () -> locationLPNList)
                .create();
    }

    private LocationLPN createLocationLPN(
            final Long itemId,
            final LocalDateTime expirationAt,
            final Integer inventoryQuantity) {
        final UsagePurpose usagePurpose = UsagePurpose.STOW;
        final Location location = createLocation(usagePurpose);
        final LPN lpn = createLPN(expirationAt);
        return Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getLpn), () -> lpn)
                .supply(Select.field(LocationLPN::getLocation), () -> location)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> inventoryQuantity)
                .supply(Select.field(LocationLPN::getItemId), () -> itemId)
                .create();
    }

    private Location createLocation(final UsagePurpose usagePurpose) {
        return Instancio.of(Location.class)
                .supply(Select.field(Location::getUsagePurpose), () -> usagePurpose)
                .create();
    }

    private LPN createLPN(final LocalDateTime expirationAt) {
        return Instancio.of(LPN.class)
                .supply(Select.field(LPN::getExpirationAt), () -> expirationAt)
                .create();
    }

    private OutboundItem createOutboundItem(
            final long itemId,
            final int outboundQuantity) {
        final Item item = createItem(itemId);
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundQuantity)
                .supply(Select.field(OutboundItem::getItem), () -> item)
                .create();
    }

    private Item createItem(final Long itemId) {
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getId), () -> itemId)
                .create();
    }


    @Test
    @DisplayName("출고하려는 주문이 집품 가능한지 확인. - 유통기한 만료")
    void validate_expired() {
        final long itemId = 1L;
        final LocalDateTime expirationAt = LocalDateTime.now().minusDays(1);
        final int inventoryQuantity = 1;
        final LocationLPN locationLPN = createLocationLPN(
                itemId,
                expirationAt,
                inventoryQuantity);

        final int outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(
                itemId,
                outboundQuantity);

        assertThatThrownBy(() -> {
            PickingAllocationValidator.validate(
                    createPickingReadyStatusOutbound(outboundItem),
                    List.of(locationLPN));
        }).isInstanceOf(NotEnoughInventoryException.class)
                .hasMessageContaining("집품할 상품의 재고가 부족합니다.");
    }

    @Test
    @DisplayName("출고하려는 주문이 집품 가능한지 확인. - 재고 부족")
    void validate_inventory() {
        final long itemId = 1L;
        final LocalDateTime expirationAt = LocalDateTime.now().minusDays(1);
        final int inventoryQuantity = 1;
        final LocationLPN locationLPN = createLocationLPN(
                itemId,
                expirationAt,
                inventoryQuantity);

        final int outboundQuantity = 2;
        final OutboundItem outboundItem = createOutboundItem(
                itemId,
                outboundQuantity);

        assertThatThrownBy(() -> {
            PickingAllocationValidator.validate(
                    createPickingReadyStatusOutbound(outboundItem),
                    List.of(locationLPN));
        }).isInstanceOf(NotEnoughInventoryException.class)
                .hasMessageContaining("집품할 상품의 재고가 부족합니다.");
    }
}