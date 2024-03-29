package leejoongseok.wms.location.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.common.retry.RetryOnOptimisticLockingFailure;
import leejoongseok.wms.location.domain.InventoryTransferManager;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationRepository;
import leejoongseok.wms.location.exception.LocationBarcodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능
 * ex) 진열구역에 집품해야할 재고를 보충하기 위해 보충구역에서 재고 일부를 진열구역으로 이동시킨다.
 */
@RestController
@RequiredArgsConstructor
public class TransferInventory {
    private final LocationRepository locationRepository;

    @RetryOnOptimisticLockingFailure
    @Transactional
    @PostMapping("/locations/location-lpns/transfer")
    public void request(@RequestBody @Valid final Request request) {
        final Location fromLocation = getLocation(request.fromLocationBarcode);
        final LocationLPN targetLocationLPN = fromLocation.getLocationLPN(request.targetLPNId);
        final Location toLocation = getLocation(request.toLocationBarcode);

        InventoryTransferManager.transfer(
                toLocation,
                targetLocationLPN,
                request.transferQuantity);
    }

    private Location getLocation(final String fromLocationBarcode) {
        return locationRepository.findByLocationBarcodeAndFetchJoinLocationLPNList(fromLocationBarcode)
                .orElseThrow(() -> new LocationBarcodeNotFoundException(fromLocationBarcode));
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
