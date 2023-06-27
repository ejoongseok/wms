package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.outbound.domain.EfficiencyLocationLPNSorter;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.Picking;
import leejoongseok.wms.outbound.domain.PickingAllocationValidator;
import leejoongseok.wms.outbound.domain.PickingCreator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PickingAllocator {
    private final EfficiencyLocationLPNSorter efficiencyLocationLPNSorter = new EfficiencyLocationLPNSorter();
    private final PickingCreator pickingCreator = new PickingCreator();
    private final PickingAllocationValidator pickingAllocationValidator = new PickingAllocationValidator();

    /**
     * 집품을 할당한다.
     */
    public void allocate(
            final Outbound outbound,
            final List<LocationLPN> locationLPNList) {
        final List<OutboundItem> outboundItems = outbound.getOutboundItems();
        pickingAllocationValidator.validate(
                outboundItems,
                locationLPNList);

        for (final OutboundItem outboundItem : outboundItems) {
            final List<LocationLPN> sortedLocationLPNList = filterByItemIdAndSortLocationLPNList(
                    locationLPNList,
                    outboundItem.getItemId());

            final List<Picking> pickings = pickingCreator.createPickings(
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
    private List<LocationLPN> filterByItemIdAndSortLocationLPNList(
            final List<LocationLPN> locationLPNList,
            final Long itemId) {
        final List<LocationLPN> locationLPNS = locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getItemId().equals(itemId))
                .toList();
        return efficiencyLocationLPNSorter.sort(
                locationLPNS);
    }

}
