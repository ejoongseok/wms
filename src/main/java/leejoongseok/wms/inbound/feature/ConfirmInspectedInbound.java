package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.exception.InboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 입고 검수 확정 기능을 수행하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
public class ConfirmInspectedInbound {
    private final InboundRepository inboundRepository;

    @Transactional
    @PostMapping("/inbounds/{inboundId}/confirm-inspected")
    public void request(@PathVariable final Long inboundId) {
        final Inbound inbound = getInbound(inboundId);

        inbound.confirmInspected();
    }

    private Inbound getInbound(final Long inboundId) {
        return inboundRepository.findById(inboundId)
                .orElseThrow(() -> new InboundIdNotFoundException(inboundId));
    }
}
