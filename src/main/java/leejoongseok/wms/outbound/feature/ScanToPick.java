package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.outbound.domain.PickingRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScanToPick {
    private final PickingRepository pickingRepository;

    public void request(final Request request) {

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
