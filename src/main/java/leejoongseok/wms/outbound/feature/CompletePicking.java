package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 출고해야할 상품의 집품이 모두 완료되면 집품완료를 할 수 있다.
 */
@Component
@RequiredArgsConstructor
public class CompletePicking {
    private final OutboundRepository outboundRepository;

    @Transactional
    public void request(final Request request) {
        final Outbound outbound = getOutbound(request.outboundId);

        outbound.completePicking();
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }

    public record Request(
            @NotNull(message = "집품을 완료하려는 출고 ID는 필수 입니다.")
            Long outboundId) {
    }
}
