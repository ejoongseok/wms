package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 집품해야할 로케이션에 가서 LPN을 선택하고 토트에 담을 상품의 수량을 입력합니다.
 * 집품해야할 상품이 많을 경우 스캔해서 집품 수량을 올리는게 아닌 한번에 토트에 담아두고 직접 수량을 입력해서 집품을 진행합니다.
 */
public class ManualToPick {
    public void request(final Request request) {
        throw new UnsupportedOperationException("Unsupported request");
    }

    public record Request(
            @NotNull(message = "피킹 ID는 필수 입니다.")
            Long pickingId,
            @NotNull(message = "로케이션 LPN ID는 필수 입니다.")
            Long locationLPNId,
            @NotNull(message = "집품할 수량은 필수 입니다.")
            @Min(value = 1, message = "집품할 수량은 1개 이상이어야 합니다.")
            Integer pickedQuantity) {
    }
}
