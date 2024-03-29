package leejoongseok.wms.location.domain;

import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public final class LocationLPNList {
    private final List<LocationLPN> locationLPNList;

    public LocationLPNList(final List<LocationLPN> locationLPNList) {
        Assert.notEmpty(locationLPNList, "LocationLPN 목록이 존재하지 않습니다.");
        this.locationLPNList = locationLPNList;
    }

    public List<LocationLPN> listFromEfficientlySorted(final Long itemId) {
        final List<LocationLPN> locationLPNList = listFrom(itemId);
        return sort(locationLPNList);
    }

    private List<LocationLPN> listFrom(final Long itemId) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .toList();
    }

    /**
     * 집품하기 효율적으로 LocationLPN을 정렬한다.
     * 1. 유통기한이 가장 짧은 순서 (선입선출)
     * 2. 재고수량이 많은 순서 (그래야 최대한 적은 로케이션 LPN으로 집품이 가능하니까)
     * 3. 로케이션 바코드 명으로 정렬 (보통 로케이션 바코드는 A -> Z 순서로 정렬되어 있음)
     */
    private List<LocationLPN> sort(final List<LocationLPN> locationLPNList) {
        validateParameter(locationLPNList);
        return locationLPNList.stream()
                .sorted(Comparator.comparing(LocationLPN::getExpirationAt).reversed()
                        .thenComparing(LocationLPN::getInventoryQuantity).reversed()
                        .thenComparing(LocationLPN::getLocationBarcode))
                .toList();
    }

    private void validateParameter(final List<LocationLPN> locationLPNList) {
        Assert.notEmpty(locationLPNList, "정렬하려는 LocationLPN이 존재하지 않습니다.");
        final boolean isAllFreshLocationLPN = locationLPNList.stream()
                .allMatch(locationLPN -> locationLPN.isFreshLPNBy(LocalDateTime.now()));
        if (!isAllFreshLocationLPN) {
            throw new IllegalArgumentException("유통기한이 지난 LPN이 존재합니다.");
        }
    }

    public List<LocationLPN> locationLPNList() {
        return locationLPNList;
    }

}