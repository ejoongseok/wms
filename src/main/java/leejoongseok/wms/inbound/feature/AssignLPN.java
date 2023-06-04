package leejoongseok.wms.inbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.exception.InboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AssignLPN {
    private final InboundRepository inboundRepository;

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inbounds/{inboundId}/inbound-items/{inboundItemId}/assign-lpn")
    public void request(
            @PathVariable final Long inboundId,
            @PathVariable final Long inboundItemId,
            @RequestBody @Valid final Request request) {
        final Inbound inbound = getInbound(inboundId);
        inbound.assignLPN(
                inboundItemId,
                request.lpnBarcode,
                request.expirationAt);
    }

    private Inbound getInbound(final Long inboundId) {
        return inboundRepository.findById(inboundId)
                .orElseThrow(() -> new InboundIdNotFoundException(inboundId));
    }

    public record Request(
            @NotBlank(message = "LPN 바코드는 필수입니다.")
            String lpnBarcode,
            @Future(message = "유통기한은 현재 시간보다 나중이어야 합니다.")
            LocalDateTime expirationAt) {
    }
}
