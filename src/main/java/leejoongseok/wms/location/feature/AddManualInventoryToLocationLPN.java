package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 로케이션 LPN에 재고 수량을 직접 추가한다.
 */
public class AddManualInventoryToLocationLPN {
    public void request(final Request request) {

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
