package leejoongseok.wms.inbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.exception.InboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RejectInbound {
    private final InboundRepository inboundRepository;

    @Transactional
    @PostMapping("/inbounds/{inboundId}/reject")
    public void request(
            @PathVariable final Long inboundId,
            @RequestBody @Valid final Request request) {
        final Inbound inbound = inboundRepository.findById(inboundId)
                .orElseThrow(() -> new InboundIdNotFoundException(inboundId));

        inbound.reject(request.rejectionReasons);
    }

    public record Request(
            @NotBlank(message = "입고 거부 사유를 입력해주세요.")
            String rejectionReasons
    ) {
    }
}
