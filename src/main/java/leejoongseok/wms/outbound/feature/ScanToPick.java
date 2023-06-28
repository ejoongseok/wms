package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.Picking;
import leejoongseok.wms.outbound.domain.PickingRepository;
import leejoongseok.wms.outbound.exception.LocationLPNBarcodeNotFoundException;
import leejoongseok.wms.outbound.exception.PickingIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 집품해야할 로케이션에 가서 LPN을 스캔하면 하고 토트에 상품을 담으면 피킹이 진행됩니다.
 */
@RestController
@RequiredArgsConstructor
public class ScanToPick {
    private final PickingRepository pickingRepository;
    private final LocationLPNRepository locationLPNRepository;

    @Transactional
    @PostMapping("/outbounds/pickings/scan-to-pick")
    public void request(@RequestBody @Valid final Request request) {
        final LocationLPN locationLPN = getLocationLPN(request.locationBarcode, request.lpnBarcode);
        final Picking picking = getPicking(request.pickingId);

        picking.increasePickedQuantity(locationLPN);
    }

    private LocationLPN getLocationLPN(
            final String locationBarcode,
            final String lpnBarcode) {
        return locationLPNRepository.findByLocationBarcodeAndLPNBarcode(locationBarcode, lpnBarcode)
                .orElseThrow(() -> new LocationLPNBarcodeNotFoundException(locationBarcode, lpnBarcode));
    }

    private Picking getPicking(final Long pickingId) {
        return pickingRepository.findById(pickingId)
                .orElseThrow(() -> new PickingIdNotFoundException(pickingId));
    }

    public record Request(
            @NotNull(message = "피킹 ID는 필수 입니다.")
            Long pickingId,
            @NotBlank(message = "로케이션 바코드는 필수 입니다.")
            String locationBarcode,
            @NotBlank(message = "상품의 LPN 바코드는 필수 입니다.")
            String lpnBarcode) {
    }
}
