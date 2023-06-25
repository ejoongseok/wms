package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationRepository;
import leejoongseok.wms.location.exception.LocationBarcodeNotFoundException;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AssignPickingTote {
    private final OutboundRepository outboundRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public void request(final Request request) {
        final Outbound outbound = getOutbound(request.outboundId);
        final Location tote = getTote(request.toteBarcode);
        outbound.assignPickingTote(tote);
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }

    private Location getTote(final String toteBarcode) {
        return locationRepository.findByLocationBarcode(toteBarcode)
                .orElseThrow(() -> new LocationBarcodeNotFoundException(toteBarcode));
    }

    public record Request(
            @NotNull(message = "출고 ID는 필수 입니다.")
            Long outboundId,
            @NotBlank(message = "토트 바코드는 필수 입니다.")
            String toteBarcode) {
    }
}
