package leejoongseok.wms.location.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.exception.LPNBarcodeNotFoundException;
import leejoongseok.wms.location.exception.LocationBarcodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로케이션에 LPN을 등록.
 * LPN이 이미 존재하는경우 LocationLPN의 inventory quantity만 증가.
 * LPN이 존재하지 않으면 LocationLPN을 새로 생성해서 등록.
 */
@RestController
@RequiredArgsConstructor
public class AssignLPNToLocation {
    private final LocationRepository locationRepository;
    private final LPNRepository lpnRepository;

    @Transactional
    @PostMapping("/locations/assign-lpn")
    public void request(@RequestBody @Valid final Request request) {
        final LPN lpn = getLPN(request);
        final Location location = getLocation(request);
        location.assignLPN(lpn);
    }

    private LPN getLPN(final Request request) {
        return lpnRepository.findByLPNBarcode(request.lpnBarcode)
                .orElseThrow(() -> new LPNBarcodeNotFoundException(request.lpnBarcode));
    }

    private Location getLocation(final Request request) {
        return locationRepository.findByLocationBarcode(request.locationBarcode)
                .orElseThrow(() -> new LocationBarcodeNotFoundException(request.locationBarcode));
    }

    public record Request(
            @NotBlank(message = "로케이션에 등록할 LPN 바코드를 입력해주세요.")
            String lpnBarcode,
            @NotBlank(message = "로케이션 바코드를 입력해주세요.")
            String locationBarcode) {
    }
}
