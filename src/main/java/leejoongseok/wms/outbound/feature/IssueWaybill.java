package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import leejoongseok.wms.outbound.waybill.Waybill;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 출고에 대한 운송장을 발행하는 기능을 담당하는 핸들러
 */
@RestController
@RequiredArgsConstructor
public class IssueWaybill {
    private final OutboundRepository outboundRepository;
    private final Waybill waybill;

    @Transactional
    @PostMapping("/outbounds/{outboundId}/issue-waybill")
    public void request(@PathVariable final Long outboundId) {
        final var outbound = getOutbound(outboundId);

        final String trackingNumber = waybill.request(outbound);

        outbound.assignTrackingNumber(trackingNumber);
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }
}
