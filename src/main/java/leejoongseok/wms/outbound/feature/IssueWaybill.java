package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import leejoongseok.wms.outbound.port.WaybillRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 출고에 대한 운송장을 발행하는 기능을 담당하는 핸들러
 */
@Component
@RequiredArgsConstructor
public class IssueWaybill {
    private final OutboundRepository outboundRepository;
    private final WaybillRequester waybillRequester;

    @Transactional
    public void request(final Long outboundId) {
        final var outbound = getOutbound(outboundId);

        final String trackingNumber = waybillRequester.request(outbound);

        outbound.assignTrackingNumber(trackingNumber);
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }
}
