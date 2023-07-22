package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.common.retry.RetryOnOptimisticLockingFailure;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNList;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.location.exception.LocationLPNNotFoundException;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.domain.PickingAllocator;
import leejoongseok.wms.outbound.exception.LPNItemIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 출고에 상품에 대한 집품 목록을 할당하는 기능을 담당하는 클래스
 */
@RestController
@RequiredArgsConstructor
public class AllocatePicking {
    private final OutboundRepository outboundRepository;
    private final LPNRepository lpnRepository;
    private final LocationLPNRepository locationLPNRepository;

    /**
     * 집품 목록을 출고상품에 할당하고
     * 출고상품의 수량만큼 LocationLPN의 재고를 감소.
     * 출고의 진행상태를 집품준비로 변경
     */
    @RetryOnOptimisticLockingFailure
    @PostMapping("/outbounds/allocate-picking")
    @Transactional
    public void request(@RequestBody @Valid final Request request) {
        final Outbound outbound = getOutbound(request);
        final List<LocationLPN> locationLPNList = getLocationLPNList(
                outbound.getItemIds());

        allocatePicking(outbound, locationLPNList);
    }

    private Outbound getOutbound(final Request request) {
        return outboundRepository.findByIdAndFetchJoinOutboundItems(request.outboundId)
                .orElseThrow(() -> new IllegalArgumentException("출고 정보가 존재하지 않습니다."));
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

    private void allocatePicking(final Outbound outbound, final List<LocationLPN> locationLPNList) {
        PickingAllocator.allocate(outbound, new LocationLPNList(locationLPNList));
        outbound.deductAllocatedInventory();
        outbound.startPickingProgress();
    }

    public record Request(
            @NotNull(message = "출고 ID는 필수 입니다.")
            Long outboundId) {
    }
}
