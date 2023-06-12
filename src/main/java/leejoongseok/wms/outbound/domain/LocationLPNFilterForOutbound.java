package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 출고 생성에 사용가능한 LocationLPN 목록으로 필터링하는 클래스
 */
public class LocationLPNFilterForOutbound {
    /**
     * 출고 생성에 사용가능하도록 LocationLPN 목록을 필터 후 반환한다.
     */
    public List<LocationLPN> filter(
            final List<LocationLPN> locationLPNList,
            final LocalDateTime filterAt) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getLpn().getExpirationAt().isAfter(filterAt))
                .filter(locationLPN -> 0 < locationLPN.getInventoryQuantity())
                .toList();
    }
}
