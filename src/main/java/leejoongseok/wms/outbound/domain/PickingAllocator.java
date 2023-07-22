package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNList;

import java.util.List;

public enum PickingAllocator {
    ;

    /**
     * 집품을 할당한다.
     */
    public static void allocate(
            final Outbound outbound,
            final LocationLPNList locationLPNList) {
        PickingAllocationValidator.validate(
                outbound,
                locationLPNList.locationLPNList());

        for (final OutboundItem outboundItem : outbound.getOutboundItems()) {
            final Long itemId = outboundItem.getItemId();
            final List<LocationLPN> sortedLocationLPNList = locationLPNList.getEfficiencyLocationLPNList(itemId);

            // TODO Pickings.of()를 사용하도록 변경
            final List<Picking> pickings = PickingCreator.createPickings(
                    outboundItem.getItemId(),
                    outboundItem.getOutboundQuantity(),
                    sortedLocationLPNList
            );

            outboundItem.assignPickings(pickings);
        }

    }

}
