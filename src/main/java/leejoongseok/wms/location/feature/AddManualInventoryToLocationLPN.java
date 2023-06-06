package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationRepository;
import leejoongseok.wms.location.exception.LPNBarcodeNotFoundException;
import leejoongseok.wms.location.exception.LocationBarcodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로케이션 LPN에 재고 수량을 직접 추가한다.
 */
@Component
@RequiredArgsConstructor
public class AddManualInventoryToLocationLPN {
    private final LocationRepository locationRepository;
    private final LPNRepository lpnRepository;

    @Transactional
    public void request(final Request request) {
        final LPN lpn = getLPN(request.lpnBarcode());
        final Location location = getLocation(request.locationBarcode());
        location.addManualInventoryToLocationLPN(lpn, request.inventoryQuantity());
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
            String locationBarcode,
            @NotNull(message = "로케이션 LPN에 추가할 재고 수량을 입력해주세요.")
            @Min(value = 1, message = "로케이션 LPN에 추가할 재고 수량은 1 이상이어야 합니다.")
            Integer inventoryQuantity) {
    }
}
