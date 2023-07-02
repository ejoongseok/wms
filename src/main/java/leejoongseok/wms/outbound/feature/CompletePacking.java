package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.workload.MeasureWorkLoad;
import leejoongseok.wms.common.workload.WorkloadType;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompletePacking {
    private final OutboundRepository outboundRepository;

    @MeasureWorkLoad(type = WorkloadType.PACKING)
    @Transactional
    @PostMapping("/outbounds/{outboundId}/packings/complete")
    public void request(@PathVariable final Long outboundId) {
        final Outbound outbound = getOutbound(outboundId);

        outbound.completePacking();
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }
}
