package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;

import java.util.List;

public final class LocationLPNList {
    private final List<LocationLPN> locationLPNList;

    public LocationLPNList(final List<LocationLPN> locationLPNList) {
        this.locationLPNList = locationLPNList;
    }

    List<LocationLPN> getEfficiencyLocationLPNList(final Long itemId) {
        final List<LocationLPN> locationLPNS = listFrom(itemId);
        return EfficiencyLocationLPNSorter.sort(
                locationLPNS);
    }

    private List<LocationLPN> listFrom(final Long itemId) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .toList();
    }

    public List<LocationLPN> locationLPNList() {
        return locationLPNList;
    }

}