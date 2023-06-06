package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;

import java.time.LocalDateTime;
import java.util.List;

public class OutboundLocationLPNList {

    public void validateAvailableInventoryQuantity(final List<LocationLPN> locationLPNList, final Integer orderQuantity) {
        // 유통기한이 지난 로케이션 LPN을 제외한다.
        final List<LocationLPN> nonExpiredLocationLPNList = filterByNonExpiredLocationLPN(locationLPNList);
        // 유통기한이 지난 로케이션 LPN을 제외한 재고 수량을 계산한다.
        final int availableInventoryQuantity = calculateAvailableInventoryQuantity(nonExpiredLocationLPNList);
        // 출고 요청 수량이 출고 가능한 재고 수량보다 크면 예외를 발생시킨다.
        if (availableInventoryQuantity < orderQuantity) {
            throw new IllegalArgumentException(
                    "출고 가능한 재고가 부족합니다. " +
                            "가용 가능한 재고 수량: " + availableInventoryQuantity +
                            " 출고 요청 수량: " + orderQuantity);
        }
    }

    private List<LocationLPN> filterByNonExpiredLocationLPN(final List<LocationLPN> locationLPNList) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getLpn().getExpirationAt().isAfter(LocalDateTime.now()))
                .toList();
    }

    private int calculateAvailableInventoryQuantity(final List<LocationLPN> nonExpiredLocationLPNList) {
        final int availableInventoryQuantity = nonExpiredLocationLPNList.stream()
                .mapToInt(LocationLPN::getInventoryQuantity)
                .sum();
        return availableInventoryQuantity;
    }
}