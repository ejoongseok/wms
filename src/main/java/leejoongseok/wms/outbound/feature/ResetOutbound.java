package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 출고를 초기화하면 재고가 변경되거나 하지는 않는다.
 * 출고를 초기화 하기위해서는 출고 중지상태에서만 가능하다.
 * 출고를 초기화하면 출고에 할당된 토트는 해제된다.
 * 출고를 초기화하면 출고와 연관된 picking도 삭제 된다.
 */
@RestController
@RequiredArgsConstructor
public class ResetOutbound {
    private final OutboundRepository outboundRepository;

    @Transactional
    @PostMapping("/outbounds/{outboundId}/reset")
    public void request(@PathVariable final Long outboundId) {
        final Outbound outbound = getOutbound(outboundId);

        outbound.reset();
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }
}
