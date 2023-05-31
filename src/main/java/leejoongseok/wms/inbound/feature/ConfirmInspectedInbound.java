package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.exception.InboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ConfirmInspectedInbound {
    private final InboundRepository inboundRepository;

    @Transactional
    public void request(final Long inboundId) {
        final Inbound inbound = inboundRepository.findById(inboundId)
                .orElseThrow(() -> new InboundIdNotFoundException(inboundId));

        inbound.confirmInspected();
    }
}
