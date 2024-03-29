package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public enum PickingCreator {
    ;

    /**
     * 출고할 주문 상품의 집품목록을 생성.
     * 1.재고수량이 출고수량보다 크거나 같으면, 하나의 Picking만 생성.
     * <p>
     * 2-1.LocationLPN이 출고수량을 충족하면, Picking 생성 후 다음 Picking 생성을 중단.
     * 2-2.LocationLPN이 출고수량을 충족하지 못하면, Picking 생성 후 다음 LocationLPN으로 이동 (2-1).
     * <p>
     * 재고수량이 출고수량보다 작으면 예외발생
     */
    public static List<Picking> createPickings(
            final Long itemId,
            final Integer outboundQuantity,
            final List<LocationLPN> locationLPNList) {
        validate(
                itemId,
                outboundQuantity,
                locationLPNList);
        final LocationLPN firstLocationLPN = locationLPNList.get(0);
        if (firstLocationLPN.getInventoryQuantity() >= outboundQuantity) {
            return List.of(new Picking(firstLocationLPN, outboundQuantity));
        }

        Integer remainingQuantity = outboundQuantity;
        final List<Picking> pickings = new ArrayList<Picking>();
        for (final LocationLPN locationLPN : locationLPNList) {
            if (isAllocationComplete(remainingQuantity)) {
                return pickings;
            }
            final Integer quantityToAllocate = Math.min(
                    locationLPN.getInventoryQuantity(),
                    remainingQuantity);
            remainingQuantity -= quantityToAllocate;
            pickings.add(new Picking(locationLPN, quantityToAllocate));
        }
        return pickings;
    }

    private static void validate(
            final Long itemId,
            final Integer outboundQuantity,
            final List<LocationLPN> locationLPNList) {
        Assert.notNull(itemId, "상품ID가 존재하지 않습니다.");
        Assert.notNull(outboundQuantity, "출고수량이 존재하지 않습니다.");
        Assert.notEmpty(locationLPNList, "LocationLPN이 존재하지 않습니다.");
        locationLPNList.stream()
                .filter(locationLPN -> !itemId.equals(locationLPN.getItemId()))
                .anyMatch(locationLPN -> {
                    throw new IllegalArgumentException("상품이 다른 LPN에 존재합니다.");
                });
        locationLPNList.stream()
                .filter(locationLPN -> !locationLPN.isFreshLPNBy(LocalDateTime.now()))
                .anyMatch(locationLPN -> {
                    throw new IllegalArgumentException("유통기한이 지난 LPN이 존재합니다.");
                });
        final int totalInventoryQuantity = locationLPNList.stream()
                .mapToInt(locationLPN -> locationLPN.getInventoryQuantity())
                .sum();
        if (totalInventoryQuantity < outboundQuantity) {
            throw new IllegalStateException(
                    "집품해야할 수량이 재고수량보다 많습니다. " +
                            "상품ID: %d, 재고수량: %d, 집품해야할 수량: %d".formatted(
                                    itemId,
                                    totalInventoryQuantity,
                                    outboundQuantity));
        }
    }

    private static boolean isAllocationComplete(final Integer remainingQuantity) {
        return 0 >= remainingQuantity;
    }
}