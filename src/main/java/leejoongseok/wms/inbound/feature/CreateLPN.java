package leejoongseok.wms.inbound.feature;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.exception.InboundIdNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreateLPN {
    private final InboundRepository inboundRepository;

    public void request(final Request request) {
        final Inbound inbound = getInbound(request);
        final LPN lpn = inbound.createLPN(
                request.inboundItemId,
                request.lpnBarcode,
                request.expirationAt);

    }

    private Inbound getInbound(final Request request) {
        return inboundRepository.findById(request.inboundId)
                .orElseThrow(() -> new InboundIdNotFoundException(request.inboundId));
    }

    public record Request(
            @NotNull(message = "입고 ID는 필수입니다.")
            Long inboundId,
            @NotNull(message = "입고 아이템 ID는 필수입니다.")
            Long inboundItemId,
            @NotBlank(message = "LPN 바코드는 필수입니다.")
            String lpnBarcode,
            @Future(message = "유통기한은 현재 시간보다 나중이어야 합니다.")
            LocalDateTime expirationAt) {
    }
}
