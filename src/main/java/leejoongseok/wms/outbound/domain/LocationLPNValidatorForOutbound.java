package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;
import org.springframework.util.Assert;

import java.util.List;

/**
 * LocationLPN 재고 수량이 출고 요청 수량에 충분한지 검증하는 클래스
 */
public enum LocationLPNValidatorForOutbound {
    ;

    /**
     * LocationLPN 목록 재고 수량이 출고 요청 수량에 충분한지 검증한다.
     */
    public static void validate(
            final List<LocationLPN> locationLPNList,
            final Integer orderQuantity) {
        Assert.notEmpty(locationLPNList, "출고 가능한지 검증할 로케이션 LPN 목록이 비어있습니다.");
        Assert.notNull(orderQuantity, "출고 요청 수량이 비어있습니다.");
        if (0 >= orderQuantity) {
            throw new IllegalArgumentException("출고 요청 수량이 0보다 작거나 같습니다.");
        }
        final int availableInventoryQuantity = calculateTotalInventoryQuantity(
                locationLPNList);
        if (availableInventoryQuantity < orderQuantity) {
            throw new IllegalArgumentException(
                    "출고 가능한 재고가 부족합니다. " +
                            "출고 가능한 재고 수량: " + availableInventoryQuantity +
                            " 출고 요청 수량: " + orderQuantity);
        }
    }

    private static int calculateTotalInventoryQuantity(
            final List<LocationLPN> locationLPNList) {
        return locationLPNList.stream()
                .mapToInt(LocationLPN::getInventoryQuantity)
                .sum();
    }
}
