package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.outbound.exception.NotEnoughInventoryException;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 출고하려는 주문이 집품 가능한지 확인.
 */
public class PickingAllocationValidator {
    public void validate(
            final Outbound outbound,
            final List<LocationLPN> locationLPNList) {
        Assert.notNull(outbound, "검증할 출고가 존재하지 않습니다.");
        if (!outbound.isPickingReadyStatus()) {
            throw new IllegalStateException("집품 대기 상태에서만 집품을 할당할 수 있습니다." +
                    "출고 상태: %s".formatted(outbound.getOutboundStatus()));
        }
        final List<OutboundItem> outboundItems = outbound.getOutboundItems();
        Assert.notEmpty(outboundItems, "검증할 출고 상품이 존재하지 않습니다.");
        Assert.notEmpty(locationLPNList, "검증할 LocationLPN이 존재하지 않습니다.");
        for (final OutboundItem outboundItem : outboundItems) {
            final long availableInventoryQuantity = calculateAvailableInventoryQuantity(
                    locationLPNList,
                    outboundItem.getItemId());

            if (isEnoughInventoryQuantity(outboundItem.getOutboundQuantity(), availableInventoryQuantity)) {
                continue;
            }

            throw new NotEnoughInventoryException("집품할 상품의 재고가 부족합니다." +
                    "상품ID: %d, 재고수량: %d, 필요한 수량: %d".formatted(
                            outboundItem.getItemId(),
                            availableInventoryQuantity,
                            outboundItem.getOutboundQuantity()));
        }
    }

    private long calculateAvailableInventoryQuantity(
            final List<LocationLPN> locationLPNList,
            final Long itemId) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .filter(locationLPN -> locationLPN.isPickingAllocatable(
                        LocalDateTime.now()))
                .mapToLong(locationLPN -> locationLPN.getInventoryQuantity())
                .sum();
    }

    private boolean isEnoughInventoryQuantity(
            final Integer outboundQuantity,
            final Long availableInventoryQuantity) {
        return outboundQuantity <= availableInventoryQuantity;
    }

}
