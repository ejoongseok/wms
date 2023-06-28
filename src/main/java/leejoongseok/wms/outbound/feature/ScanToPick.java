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

@RestController
@RequiredArgsConstructor
public class ScanToPick {
    private final PickingRepository pickingRepository;
    private final LocationLPNRepository locationLPNRepository;

    @Transactional
    @PostMapping("/outbounds/pickings/scan-to-pick")
    public void request(@RequestBody @Valid final Request request) {
        final Picking picking = getPicking(request.pickingId);
        final LocationLPN locationLPN = getLocationLPN(request);

        picking.increasePickedQuantity(locationLPN);
    }

    private LocationLPN getLocationLPN(final Request request) {
        return locationLPNRepository.findByLocationBarcodeAndLPNBarcode(request.locationBarcode, request.lpnBarcode)
                .orElseThrow(() -> new LocationLPNBarcodeNotFoundException(request.locationBarcode, request.lpnBarcode));
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
