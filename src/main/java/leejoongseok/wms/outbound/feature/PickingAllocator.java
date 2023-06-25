package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.exception.NotEnoughInventoryException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
public class PickingAllocator {
    public void allocate(
            final Outbound outbound,
            final List<LocationLPN> locationLPNList) {
        final List<OutboundItem> outboundItems = outbound.getOutboundItems();
        final List<LocationLPN> pickingAllocatableLocationLPNList = filterByPickingAllocatable(
                locationLPNList);
        validatePickingAllocatable(
                outboundItems,
                pickingAllocatableLocationLPNList);

        for (final OutboundItem outboundItem : outboundItems) {
            final Long itemId = outboundItem.getItemId();
            final List<LocationLPN> locationLPNS = getLocationLPNListByItemId(locationLPNList, itemId);

            final List<LocationLPN> sortedLocationLPNList = sort(locationLPNS);
        }

    }

    private List<LocationLPN> filterByPickingAllocatable(
            final List<LocationLPN> locationLPNList) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.isPickingAllocatable(
                        LocalDateTime.now()))
                .toList();
    }

    private void validatePickingAllocatable(
            final List<OutboundItem> outboundItems,
            final List<LocationLPN> locationLPNList) {
        for (final OutboundItem outboundItem : outboundItems) {
            final Long itemId = outboundItem.getItemId();
            final long availableInventoryQuantity = calculateAvailableInventoryQuantity(
                    locationLPNList,
                    itemId);
            if (!isEnoughInventoryQuantity(
                    outboundItem.getOutboundQuantity(),
                    availableInventoryQuantity)) {
                throw new NotEnoughInventoryException("집품할 상품의 재고가 부족합니다." +
                        "상품ID: %d, 재고수량: %d, 필요한 수량: %d".formatted(
                                itemId,
                                availableInventoryQuantity,
                                outboundItem.getOutboundQuantity()));
            }
        }
    }

    private long calculateAvailableInventoryQuantity(
            final List<LocationLPN> locationLPNList,
            final Long itemId) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .mapToLong(locationLPN -> locationLPN.getInventoryQuantity())
                .sum();
    }

    private boolean isEnoughInventoryQuantity(
            final Integer outboundQuantity,
            final Long availableInventoryQuantity) {
        return outboundQuantity <= availableInventoryQuantity;
    }

    private List<LocationLPN> getLocationLPNListByItemId(final List<LocationLPN> locationLPNList, final Long itemId) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .toList();
    }

    /**
     * 1. 유통기한이 가장 빠른 순서
     * 2. 재고수량이 많은 순서
     * 3. 로케이션 바코드 순서 A -> Z
     */
    private List<LocationLPN> sort(final List<LocationLPN> locationLPNS) {
        return locationLPNS.stream()
                .sorted(Comparator.comparing(LocationLPN::getExpirationAt)
                        .thenComparing(LocationLPN::getInventoryQuantity).reversed()
                        .thenComparing(LocationLPN::getLocationBarcode))
                .toList();
    }
}
