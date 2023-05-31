package leejoongseok.wms.inbound;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class CreateInbound {
    private final InboundRepository inboundRepository;

    public CreateInbound(final InboundRepository inboundRepository) {
        this.inboundRepository = inboundRepository;
    }

    public void request(final Request request) {
        final Inbound inbound = request.toEntity();
        inboundRepository.save(inbound);
    }

    public record Request(
            LocalDateTime orderRequestAt,
            LocalDateTime estimatedArrivalAt,
            BigDecimal totalAmount) {
        Inbound toEntity() {
            return new Inbound(
                    orderRequestAt,
                    estimatedArrivalAt,
                    totalAmount
            );
        }
    }
}
