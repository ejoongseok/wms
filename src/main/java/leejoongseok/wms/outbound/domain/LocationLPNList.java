package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;

import java.util.List;

public record LocationLPNList(List<LocationLPN> locationLPNList) {
    static List<LocationLPN> getEfficiencyLocationLPNList(final LocationLPNList locationLPNList, final Long itemId) {
        final List<LocationLPN> locationLPNS = locationLPNList.locationLPNList().stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .toList();
        return EfficiencyLocationLPNSorter.sort(
                locationLPNS);
    }
}