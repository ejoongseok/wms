package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.NotBlank;

/**
 * 재고를 조정한다.
 */
public class AdjustInventory {


    public void request(final Request request) {
        throw new UnsupportedOperationException("Unsupported request");
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
