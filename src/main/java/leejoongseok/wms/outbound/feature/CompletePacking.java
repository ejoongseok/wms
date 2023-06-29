package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompletePacking {
    private final OutboundRepository outboundRepository;

    public void request(final Long outboundId) {
        final Outbound outbound = getOutbound(outboundId);

        outbound.completePacking();
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }
}
