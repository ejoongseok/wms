package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.location.domain.LocationRepository;
import leejoongseok.wms.location.exception.LocationBarcodeNotFoundException;
import leejoongseok.wms.location.exception.LocationLPNNotFoundException;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.LPNItemIdNotFoundException;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 출고에 피킹 토트를 할당하는 기능을 담당하는 핸들러
 */
@RestController
@RequiredArgsConstructor
public class AssignPickingTote {
    private final OutboundRepository outboundRepository;
    private final LocationRepository locationRepository;
    private final PickingAllocator pickingAllocator;
    private final LPNRepository lpnRepository;
    private final LocationLPNRepository locationLPNRepository;

    @Transactional
    @PostMapping("/outbounds/assign-picking-tote")
    public void request(
            @RequestBody @Valid final Request request) {
        final Outbound outbound = getOutbound(request.outboundId);
        final Location tote = getTote(request.toteBarcode);
        outbound.assignPickingTote(tote);
        allocatePicking(outbound);
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }

    private Location getTote(final String toteBarcode) {
        return locationRepository.findByLocationBarcode(toteBarcode)
                .orElseThrow(() -> new LocationBarcodeNotFoundException(toteBarcode));
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

    public record Request(
            @NotNull(message = "출고 ID는 필수 입니다.")
            Long outboundId,
            @NotBlank(message = "토트 바코드는 필수 입니다.")
            String toteBarcode) {

    }
}
