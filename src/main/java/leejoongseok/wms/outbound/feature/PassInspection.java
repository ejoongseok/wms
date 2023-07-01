package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 집품한 상품이 출고해야하는 상품이 맞는지, 파손이나 누락이 없는지 검사에 통과한다.
 */
@Component
@RequiredArgsConstructor
public class PassInspection {
    private final OutboundRepository outboundRepository;

    @Transactional
    public void request(final Long outboundId) {
        final Outbound outbound = getOutbound(outboundId);
        outbound.passInspection();
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }
}
