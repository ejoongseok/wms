package leejoongseok.wms.location.domain;

import org.springframework.util.Assert;

public enum InventoryTransferManager {
    ;

    /**
     * A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능
     *
     * @param fromLocation
     * @param toLocation
     * @param targetLPNId
     * @param transferQuantity
     */
    public static void transfer(
            final Location fromLocation,
            final Location toLocation,
            final Long targetLPNId,
            final Integer transferQuantity) {
        validateTransfer(fromLocation, toLocation, targetLPNId, transferQuantity);
        fromLocation.decreaseInventory(targetLPNId, transferQuantity);
        final LocationLPN locationLPN = fromLocation.getLocationLPN(targetLPNId);
        toLocation.increaseInventory(locationLPN, transferQuantity);
    }

    private static void validateTransfer(final Location fromLocation, final Location toLocation, final Long targetLPNId, final Integer transferQuantity) {
        Assert.notNull(fromLocation, "출발지 로케이션은 필수 입니다.");
        Assert.notNull(toLocation, "도착지 로케이션은 필수 입니다.");
        Assert.notNull(targetLPNId, "이동할 재고의 LPN ID는 필수 입니다.");
        Assert.notNull(transferQuantity, "이동할 재고의 수량은 필수 입니다.");
        if (0 >= transferQuantity) {
            throw new IllegalArgumentException("이동할 재고의 수량은 0보다 커야 합니다.");
        }
    }
}