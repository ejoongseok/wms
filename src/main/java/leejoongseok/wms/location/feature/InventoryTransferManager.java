package leejoongseok.wms.location.feature;

import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;

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
    static void transfer(
            final Location fromLocation,
            final Location toLocation,
            final Long targetLPNId,
            final Integer transferQuantity) {
        fromLocation.decreaseInventory(targetLPNId, transferQuantity);
        final LocationLPN locationLPN = fromLocation.getLocationLPN(targetLPNId);
        toLocation.increaseInventory(locationLPN, transferQuantity);
    }
}