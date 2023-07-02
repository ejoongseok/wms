package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.location.domain.AdjustInventoryHistory;
import leejoongseok.wms.location.domain.AdjustInventoryHistoryRepository;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.exception.LocationLPNBarcodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 재고를 조정한다.
 */
@Component
@RequiredArgsConstructor
public class AdjustInventory {
    private final LocationLPNRepository locationLPNRepository;
    private final AdjustInventoryHistoryRepository adjustInventoryHistoryRepository;

    @Transactional
    public void request(final Request request) {
        final LocationLPN locationLPN = getLocationLPN(request.locationBarcode, request.lpnBarcode);
        final Integer beforeInventoryQuantity = locationLPN.getInventoryQuantity();
        locationLPN.adjustQuantity(request.quantity);

        saveHistory(
                locationLPN,
                beforeInventoryQuantity,
                request.reason);
    }

    private void saveHistory(
            final LocationLPN locationLPN,
            final Integer beforeInventoryQuantity,
            final String reason) {
        final AdjustInventoryHistory adjustInventoryHistory = new AdjustInventoryHistory(
                locationLPN.getId(),
                locationLPN.getLocationBarcode(),
                locationLPN.getLpnBarcode(),
                beforeInventoryQuantity,
                locationLPN.getInventoryQuantity(),
                reason
        );
        adjustInventoryHistoryRepository.save(adjustInventoryHistory);
    }

    private LocationLPN getLocationLPN(
            final String locationBarcode,
            final String lpnBarcode) {
        return locationLPNRepository.findByLocationBarcodeAndLPNBarcode(locationBarcode, lpnBarcode)
                .orElseThrow(() -> new LocationLPNBarcodeNotFoundException(locationBarcode, lpnBarcode));
    }

    public record Request(
            @NotBlank(message = "로케이션 바코드는 필수입니다.")
            String locationBarcode,
            @NotBlank(message = "LPN 바코드는 필수입니다.")
            String lpnBarcode,
            @NotBlank(message = "재고를 변경할 수량은 필수입니다.")
            Integer quantity,
            @NotBlank(message = "재고를 변경하는 이유는 필수입니다.")
            String reason) {
    }
}
