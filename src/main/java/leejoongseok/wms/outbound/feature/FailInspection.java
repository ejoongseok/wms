package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 집품한 출고상품의 검수가 어떠한 이유로 통과하지 못한다.
 */
@RestController
@RequiredArgsConstructor
public class FailInspection {
    private final OutboundRepository outboundRepository;


    @Transactional
    @PostMapping("/outbounds/{outboundId}/fail-inspection")
    public void request(@PathVariable final Long outboundId,
                        @RequestBody @Valid final Request request) {
        final Outbound outbound = getOutbound(outboundId);

        outbound.failInspection(request.stoppedReason);
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }

    public record Request(
            @NotBlank(message = "검수 불합격 사유는 필수 입니다.")
            String stoppedReason) {
    }
}
