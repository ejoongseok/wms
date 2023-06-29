package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 출고해야할 상품의 집품이 모두 완료되면 집품완료를 할 수 있다.
 */
@RestController
@RequiredArgsConstructor
public class CompletePicking {
    private final OutboundRepository outboundRepository;

    @Transactional
    @PostMapping("/outbounds/{outboundId}/complete-picking")
    public void request(@PathVariable final Long outboundId) {
        final Outbound outbound = getOutbound(outboundId);

        outbound.completePicking();
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }

}
