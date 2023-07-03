package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StopOutbound {
    private final OutboundRepository outboundRepository;

    @Transactional
    @PostMapping("/outbounds/{outboundId}/stop")
    public void request(
            @PathVariable final Long outboundId,
            @RequestBody @Valid final Request request) {
        final Outbound outbound = getOutbound(outboundId);

        outbound.stop(request.stoppedReason);
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }

    public record Request(String stoppedReason) {
    }
}
