package leejoongseok.wms.location.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.common.retry.RetryOnOptimisticLockingFailure;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationRepository;
import leejoongseok.wms.location.exception.LPNBarcodeNotFoundException;
import leejoongseok.wms.location.exception.LocationBarcodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로케이션에 LPN을 적재시키는 기능을 수행하는 컨트롤러 클래스.
 */
@RestController
@RequiredArgsConstructor
public class AssignLPNToLocation {
    private final LocationRepository locationRepository;
    private final LPNRepository lpnRepository;

    @RetryOnOptimisticLockingFailure
    @Transactional
    @PostMapping("/locations/assign-lpn")
    public void request(@RequestBody @Valid final Request request) {
        final LPN lpn = getLPN(request.lpnBarcode);
        final Location location = getLocation(request.locationBarcode);
        location.assignLPN(lpn);
    }

    private LPN getLPN(final String lpnBarcode) {
        return lpnRepository.findByLPNBarcode(lpnBarcode)
                .orElseThrow(() -> new LPNBarcodeNotFoundException(lpnBarcode));
    }

    private Location getLocation(final String locationBarcode) {
        return locationRepository.findByLocationBarcode(locationBarcode)
                .orElseThrow(() -> new LocationBarcodeNotFoundException(locationBarcode));
    }

    public record Request(
            @NotBlank(message = "로케이션에 등록할 LPN 바코드를 입력해주세요.")
            String lpnBarcode,
            @NotBlank(message = "로케이션 바코드를 입력해주세요.")
            String locationBarcode) {
    }
}
