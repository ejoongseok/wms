package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * A 로케이션에 재고 10개 중 5개를 B 로케이션으로 이동시키는 기능
 * ex) 진열구역에 집품해야할 재고를 보충하기 위해 보충구역에서 재고 일부를 진열구역으로 이동시킨다.
 */
public class TransferInventory {
    public void request(final Request request) {
        throw new UnsupportedOperationException("Unsupported request");
    }

    public record Request(
            @NotNull(message = "이동할 재고의 출발지 로케이션 ID는 필수 입니다.")
            Long fromLocationId,
            @NotNull(message = "이동할 재고의 도착지 로케이션 ID는 필수 입니다.")
            Long toLocationId,
            @NotNull(message = "이동할 재고의 수량은 필수 입니다.")
            @Min(value = 0, message = "이동할 재고의 수량은 0보다 커야 합니다.")
            Long transferQuantity) {
    }
}
