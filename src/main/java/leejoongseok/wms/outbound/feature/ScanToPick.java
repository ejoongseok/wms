package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ScanToPick {

    public void request(final Request request) {
        throw new UnsupportedOperationException("Unsupported request");
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
