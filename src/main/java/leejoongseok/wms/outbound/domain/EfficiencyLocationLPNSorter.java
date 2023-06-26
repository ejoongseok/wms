package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;

import java.util.Comparator;
import java.util.List;

public class EfficiencyLocationLPNSorter {

    /**
     * 1. 유통기한이 가장 짧은 순서 (선입선출)
     * 2. 재고수량이 많은 순서 (그래야 최대한 적은 로케이션 LPN으로 집품이 가능하니까)
     * 3. 로케이션 바코드 명으로 정렬 (보통 로케이션 바코드는 A -> Z 순서로 정렬되어 있음)
     */
    public List<LocationLPN> sort(final List<LocationLPN> locationLPNS) {
        return locationLPNS.stream()
                .sorted(Comparator.comparing(LocationLPN::getExpirationAt).reversed()
                        .thenComparing(LocationLPN::getInventoryQuantity).reversed()
                        .thenComparing(LocationLPN::getLocationBarcode))
                .toList();
    }
}