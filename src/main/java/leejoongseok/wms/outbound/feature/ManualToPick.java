package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.Picking;
import leejoongseok.wms.outbound.domain.PickingRepository;
import leejoongseok.wms.outbound.exception.LocationLPNIdNotFoundException;
import leejoongseok.wms.outbound.exception.PickingIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 집품해야할 로케이션에 가서 LPN을 선택하고 토트에 담을 상품의 수량을 직접 입력합니다.
 * 집품해야할 상품이 많을 경우 스캔해서 집품 수량을 올리는게 아닌 한번에 토트에 담아두고 직접 수량을 입력해서 집품을 진행합니다.
 */
@RestController
@RequiredArgsConstructor
public class ManualToPick {
    private final PickingRepository pickingRepository;
    private final LocationLPNRepository locationLPNRepository;

    @Transactional
    @PostMapping("/outbounds/pickings/manual-to-pick")
    public void request(@RequestBody @Valid final Request request) {
        final Picking picking = getPicking(request.pickingId);
        final LocationLPN locationLPN = getLocationLPN(request.locationLPNId);

        picking.addManualPickedQuantity(locationLPN, request.pickedQuantity);
    }

    private Picking getPicking(final Long pickingId) {
        return pickingRepository.findById(pickingId)
                .orElseThrow(() -> new PickingIdNotFoundException(pickingId));
    }

    private LocationLPN getLocationLPN(final Long locationLPNId) {
        return locationLPNRepository.findById(locationLPNId)
                .orElseThrow(() -> new LocationLPNIdNotFoundException(locationLPNId));
    }

    public record Request(
            @NotNull(message = "피킹 ID는 필수 입니다.")
            Long pickingId,
            @NotNull(message = "로케이션 LPN ID는 필수 입니다.")
            Long locationLPNId,
            @NotNull(message = "집품할 수량은 필수 입니다.")
            @Min(value = 1, message = "집품할 수량은 1개 이상이어야 합니다.")
            Integer pickedQuantity) {
    }
}
