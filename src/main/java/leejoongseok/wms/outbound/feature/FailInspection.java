package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * 집품한 출고상품의 검수가 어떠한 이유로 통과하지 못한다.
 */
@RequiredArgsConstructor
public class FailInspection {
    private final OutboundRepository outboundRepository;


    public void request(final Request request) {
        final Outbound outbound = getOutbound(request);

        outbound.failInspection(request.stoppedReason);
    }

    private Outbound getOutbound(final Request request) {
        return outboundRepository.findById(request.outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(request.outboundId));
    }

    public record Request(
            @NotNull(message = "출고 ID는 필수 입니다.")
            Long outboundId,
            @NotBlank(message = "검수 불합격 사유는 필수 입니다.")
            String stoppedReason) {
    }
}
