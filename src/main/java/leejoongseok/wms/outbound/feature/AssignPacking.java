package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AssignPacking {
    public void request(final Request request) {
        throw new UnsupportedOperationException("Unsupported request");
    }

    public record Request(
            @NotNull(message = "출고 ID는 필수 입니다.")
            Long outboundId,
            @NotNull(message = "포장자재 ID는 필수 입니다.")
            Long packagingMaterialId,
            @NotNull(message = "실중량은 필수 입니다.")
            @Min(value = 1, message = "실중량은 1g 이상이어야 합니다.")
            Integer realWeightInGrams) {
    }
}
