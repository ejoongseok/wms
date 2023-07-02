package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 출고에 사용가능한 LocationLPN 목록으로 필터링하는 클래스
 */
public enum LocationLPNFilterForOutbound {
    ;

    /**
     * 출고에 사용가능하도록 LocationLPN 목록을 필터 후 반환한다.
     * 유통 기한이 지난 LPN은 필터링한다.
     * 재고 수량이 0인 Location은 필터링한다.
     */
    public static List<LocationLPN> filter(
            final List<LocationLPN> locationLPNList,
            final LocalDateTime thisDateTime) {

        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.isFreshLPNBy(thisDateTime))
                .filter(locationLPN -> !locationLPN.isEmptyInventory())
                .toList();
    }
}
