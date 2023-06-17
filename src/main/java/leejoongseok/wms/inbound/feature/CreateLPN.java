package leejoongseok.wms.inbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import leejoongseok.wms.inbound.exception.InboundIdNotFoundException;
import leejoongseok.wms.inbound.exception.LPNBarcodeAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 입고 상품의 LPN 생성 기능을 수행하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
public class CreateLPN {
    private final InboundRepository inboundRepository;
    private final LPNRepository lpnRepository;

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inbounds/{inboundId}/inbound-items/{inboundItemId}/lpns")
    public void request(
            @PathVariable final Long inboundId,
            @PathVariable final Long inboundItemId,
            @RequestBody @Valid final Request request) {
        validateLPNBarcodeAlreadyExists(request.lpnBarcode);
        final Inbound inbound = getInbound(inboundId);
        final LPN lpn = inbound.createLPN(
                inboundItemId,
                request.lpnBarcode,
                request.expirationAt);
        lpnRepository.save(lpn);
    }

    private void validateLPNBarcodeAlreadyExists(
            final String lpnBarcode) {
        lpnRepository.findByLPNBarcode(lpnBarcode)
                .ifPresent(lpn -> {
                    throw new LPNBarcodeAlreadyExistsException(lpnBarcode);
                });
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
