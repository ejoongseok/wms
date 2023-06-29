package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import leejoongseok.wms.outbound.port.WaybillRequester;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IssueWaybill {
    private final OutboundRepository outboundRepository;
    private final WaybillRequester waybillRequester;

    public void request(final Long outboundId) {
        final var outbound = getOutbound(outboundId);
        final String trackingNumber = waybillRequester.request(outbound);

    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }
}
