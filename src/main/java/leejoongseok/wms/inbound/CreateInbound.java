package leejoongseok.wms.inbound;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
            BigDecimal totalAmount,
            List<ItemRequest> itemRequests
    ) {
        Inbound toEntity() {
            return new Inbound(
                    orderRequestAt,
                    estimatedArrivalAt,
                    totalAmount
            );
        }

        record ItemRequest(
                @NotNull(message = "상품 ID는 필수입니다.")
                Long itemId,
                @Positive(message = "입고 수량은 1개 이상이어야 합니다.")
                Integer receivedQuantity,
                @Positive(message = "입고 단가는 1원 이상이어야 합니다.")
                BigDecimal purchasePrice,
                String description
        ) {

        }
    }
}
