package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;

public class CreateLocation {
    public void request(final Request request) {

    }

    public record Request(
            @NotBlank(message = "로케이션 바코드는 필수입니다.")
            String locationBarcode,
            @NotNull(message = "보관 타입은 필수입니다.")
            StorageType storageType,
            @NotNull(message = "보관 목적은 필수입니다.")
            UsagePurpose usagePurpose) {
    }
}
