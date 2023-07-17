package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;

import java.util.List;
import java.util.Objects;

public final class LocationLPNList {
    private final List<LocationLPN> locationLPNList;

    public LocationLPNList(final List<LocationLPN> locationLPNList) {
        this.locationLPNList = locationLPNList;
    }

    static List<LocationLPN> getEfficiencyLocationLPNList(final LocationLPNList locationLPNList, final Long itemId) {
        final List<LocationLPN> locationLPNS = listFrom(locationLPNList, itemId);
        return EfficiencyLocationLPNSorter.sort(
                locationLPNS);
    }

    private static List<LocationLPN> listFrom(final LocationLPNList locationLPNList, final Long itemId) {
        return locationLPNList.locationLPNList().stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .toList();
    }

    public List<LocationLPN> locationLPNList() {
        return locationLPNList;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (null == obj || obj.getClass() != getClass()) return false;
        final var that = (LocationLPNList) obj;
        return Objects.equals(locationLPNList, that.locationLPNList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationLPNList);
    }

    @Override
    public String toString() {
        return "LocationLPNList[" +
                "locationLPNList=" + locationLPNList + ']';
    }

}