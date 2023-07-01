package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 집품한 출고상품의 검수가 어떠한 이유로 통과하지 못한다.
 */
public class FailInspection {


    public void request(final Request request) {
    }

    public record Request(
            @NotNull(message = "출고 ID는 필수 입니다.")
            Long outboundId,
            @NotBlank(message = "검수 불합격 사유는 필수 입니다.")
            String stoppedReason) {
    }
}
