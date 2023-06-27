package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.location.exception.LocationLPNNotFoundException;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.LPNItemIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AllocatePicking {
    private final OutboundRepository outboundRepository;
    private final PickingAllocator pickingAllocator;
    private final LPNRepository lpnRepository;
    private final LocationLPNRepository locationLPNRepository;

    @Transactional
    public void request(final Request request) {
        final Outbound outbound = getOutbound(request);
        allocatePicking(outbound);
    }

    private Outbound getOutbound(final Request request) {
        return outboundRepository.findById(request.outboundId)
                .orElseThrow(() -> new IllegalArgumentException("출고 정보가 존재하지 않습니다."));
    }

    private void allocatePicking(final Outbound outbound) {
        final List<OutboundItem> outboundItems = outbound.getOutboundItems();
        final List<Long> itemIds = outboundItems.stream()
                .map(OutboundItem::getItemId)
                .toList();
        final List<LocationLPN> locationLPNList = getLocationLPNList(itemIds);
        pickingAllocator.allocate(outbound, locationLPNList);
    }

    private List<LocationLPN> getLocationLPNList(final List<Long> itemIds) {
        final List<LPN> lpnList = getLpnList(itemIds);
        final List<Long> lpnIds = lpnList.stream()
                .map(LPN::getId)
                .toList();
        final List<LocationLPN> locationLPNList = locationLPNRepository.findByLPNIdsAndFetchJoinLPNAndLocation(
                lpnIds);
        for (final Long lpnId : lpnIds) {
            if (locationLPNList.stream().noneMatch(locationLPN -> locationLPN.getLPNId().equals(lpnId))) {
                throw new LocationLPNNotFoundException(
                        "%d에 해당하는 LocationLPN이 없습니다.".formatted(lpnId));
            }
        }
        return locationLPNList;
    }

    private List<LPN> getLpnList(final List<Long> itemIds) {
        final List<LPN> lpnList = lpnRepository.listOf(itemIds);
        for (final Long itemId : itemIds) {
            if (lpnList.stream().noneMatch(lpn -> lpn.getItemId().equals(itemId))) {
                throw new LPNItemIdNotFoundException(
                        "%d에 해당하는 LPN이 없습니다.".formatted(itemId));
            }
        }
        return lpnList;
    }

    public record Request(Long outboundId) {
    }
}
