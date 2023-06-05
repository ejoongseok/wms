package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.exception.LocationBarcodeNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignLPNToLocation {
    private final LocationRepository locationRepository;

    public void request(final Request request) {
        final Location location = getLocation(request);

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
