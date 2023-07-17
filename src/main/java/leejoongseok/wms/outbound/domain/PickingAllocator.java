package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;

import java.util.List;

public enum PickingAllocator {
    ;

    /**
     * 집품을 할당한다.
     */
    public static void allocate(
            final Outbound outbound, final LocationLPNList locationLPNList) {
        PickingAllocationValidator.validate(
                outbound,
                locationLPNList.locationLPNList());

        for (final OutboundItem outboundItem : outbound.getOutboundItems()) {
            final List<LocationLPN> sortedLocationLPNList = filterByItemIdAndSortLocationLPNList(
                    locationLPNList.locationLPNList(),
                    outboundItem.getItemId());

            final List<Picking> pickings = PickingCreator.createPickings(
                    outboundItem.getItemId(),
                    outboundItem.getOutboundQuantity(),
                    sortedLocationLPNList
            );

            outboundItem.assignPickings(pickings);
        }

    }

    /**
     * 상품ID에 해당하는 LocationLPN을 집품하기에 효율적으로 정렬후 반환한다.
     */
    private static List<LocationLPN> filterByItemIdAndSortLocationLPNList(
            final List<LocationLPN> locationLPNList,
            final Long itemId) {
        final List<LocationLPN> locationLPNS = locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .toList();
        return EfficiencyLocationLPNSorter.sort(
                locationLPNS);
    }

}
