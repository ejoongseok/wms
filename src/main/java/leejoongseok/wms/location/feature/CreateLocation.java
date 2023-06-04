package leejoongseok.wms.location.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import leejoongseok.wms.location.exception.LocationBarcodeAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateLocation {
    private final LocationRepository locationRepository;

    @Transactional
    @PostMapping("/locations")
    @ResponseStatus(HttpStatus.CREATED)
    public void request(@RequestBody @Valid final Request request) {
        locationRepository.findByLocationBarcode(request.locationBarcode)
                .ifPresent(location -> {
                    throw new LocationBarcodeAlreadyExistsException(request.locationBarcode);
                });

        final Location location = request.toEntity();

        locationRepository.save(location);
    }

    public record Request(
            @NotBlank(message = "로케이션 바코드는 필수입니다.")
            String locationBarcode,
            @NotNull(message = "보관 타입은 필수입니다.")
            StorageType storageType,
            @NotNull(message = "보관 목적은 필수입니다.")
            UsagePurpose usagePurpose) {
        public Location toEntity() {
            return new Location(
                    locationBarcode,
                    storageType,
                    usagePurpose
            );
        }
    }
}
