package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationRepository;
import leejoongseok.wms.location.exception.LocationBarcodeNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능
 * ex) 진열구역에 집품해야할 재고를 보충하기 위해 보충구역에서 재고 일부를 진열구역으로 이동시킨다.
 */
@RequiredArgsConstructor
public class TransferInventory {
    private final LocationRepository locationRepository;

    public void request(final Request request) {
        final Location fromLocation = getLocation(request.fromLocationBarcode);
        final Location toLocation = getLocation(request.toLocationBarcode);
        transfer(fromLocation, toLocation, request.targetLPNId, request.transferQuantity);
    }

    private Location getLocation(final String fromLocationBarcode) {
        return locationRepository.findByLocationBarcodeAndFetchJoinLocationLPNList(fromLocationBarcode)
                .orElseThrow(() -> new LocationBarcodeNotFoundException(fromLocationBarcode));
    }

    private void transfer(
            final Location fromLocation,
            final Location toLocation,
            final Long targetLPNId,
            final Integer transferQuantity) {
        fromLocation.decreaseInventory(targetLPNId, transferQuantity);
        final LocationLPN locationLPN = fromLocation.getLocationLPN(targetLPNId);
        toLocation.increaseInventory(locationLPN, transferQuantity);
    }

    public record Request(
            @NotBlank(message = "이동할 재고의 출발지 로케이션 바코드는 필수 입니다.")
            String fromLocationBarcode,
            @NotBlank(message = "이동할 재고의 도착지 로케이션 바코드는 필수 입니다.")
            String toLocationBarcode,
            @NotNull(message = "이동할 재고의 LPN ID는 필수 입니다.")
            Long targetLPNId,
            @NotNull(message = "이동할 재고의 수량은 필수 입니다.")
            @Min(value = 0, message = "이동할 재고의 수량은 0보다 커야 합니다.")
            Integer transferQuantity) {
    }
}
