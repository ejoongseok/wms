package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.exception.InboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfirmInspectedInbound {
    private final InboundRepository inboundRepository;

    @Transactional
    @PostMapping("/inbounds/{inboundId}/confirm-inspected")
    public void request(@PathVariable final Long inboundId) {
        final Inbound inbound = inboundRepository.findById(inboundId)
                .orElseThrow(() -> new InboundIdNotFoundException(inboundId));

        inbound.confirmInspected();
    }
}
