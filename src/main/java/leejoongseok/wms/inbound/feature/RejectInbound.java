package leejoongseok.wms.inbound.feature;

import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.exception.InboundIdNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RejectInbound {
    private final InboundRepository inboundRepository;

    public void request(
            final long inboundId,
            final Request request) {
        final Inbound inbound = inboundRepository.findById(inboundId)
                .orElseThrow(() -> new InboundIdNotFoundException(inboundId));

        inbound.reject(request.rejectionReasons);

    }

    public record Request(
            @NotBlank(message = "취소 사유를 입력해주세요.")
            String rejectionReasons
    ) {
    }
}
