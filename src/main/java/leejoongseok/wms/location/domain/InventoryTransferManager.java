package leejoongseok.wms.location.domain;

import org.springframework.util.Assert;

public enum InventoryTransferManager {
    ;

    /**
     * A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능
     *
     * @param toLocation
     * @param targetLocationLPN
     * @param transferQuantity
     */
    public static void transfer(
            final Location toLocation,
            final LocationLPN targetLocationLPN,
            final Integer transferQuantity) {
        validateTransfer(toLocation, targetLocationLPN, transferQuantity);

        targetLocationLPN.deductInventory(transferQuantity);
        toLocation.increaseInventory(targetLocationLPN, transferQuantity);
    }

    private static void validateTransfer(final Location toLocation, final LocationLPN targetLocationLPN, final Integer transferQuantity) {
        Assert.notNull(toLocation, "도착지 로케이션은 필수 입니다.");
        Assert.notNull(targetLocationLPN, "이동할 재고의 LocationLPN은 필수 입니다.");
        Assert.notNull(transferQuantity, "이동할 재고의 수량은 필수 입니다.");
        if (0 >= transferQuantity) {
            throw new IllegalArgumentException("이동할 재고의 수량은 0보다 커야 합니다.");
        }
        if (targetLocationLPN.getInventoryQuantity() < transferQuantity) {
            throw new IllegalArgumentException("이동할 재고의 수량은 재고 수량보다 클 수 없습니다.");
        }
    }
}