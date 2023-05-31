package leejoongseok.wms.inbound;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateInbound {
    private final InboundRepository inboundRepository;

    public CreateInbound(final InboundRepository inboundRepository) {
        this.inboundRepository = inboundRepository;
    }

    public void request(final Request request) {
        final Inbound inbound = request.toEntity();

    }

    public record Request(
            LocalDateTime orderRequestAt,
            LocalDateTime estimatedArrivalAt,
            BigDecimal totalAmount) {
        Inbound toEntity() {
            return new Inbound();
        }
    }
}
