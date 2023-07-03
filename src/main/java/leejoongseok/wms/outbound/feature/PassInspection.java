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
 * 집품한 상품이 출고해야하는 상품이 맞는지, 파손이나 누락이 없는지 검사에 통과한다.
 */
@RestController
@RequiredArgsConstructor
public class PassInspection {
    private final OutboundRepository outboundRepository;

    @Transactional
    @PostMapping("/outbounds/{outboundId}/pass-inspection")
    public void request(@PathVariable final Long outboundId) {
        final Outbound outbound = getOutbound(outboundId);

        outbound.passInspection();
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }
}
