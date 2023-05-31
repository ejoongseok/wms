package leejoongseok.wms.inbound;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateInbound {
    private final InboundRepository inboundRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void request(final Request request) {
        final Inbound inbound = request.toEntity();
        final List<InboundItem> inboundItems = toInboundItems(request.itemRequests);
        inbound.addInboundItems(inboundItems);
        inboundRepository.save(inbound);
    }

    private List<InboundItem> toInboundItems(final List<Request.ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(itemRequest -> new InboundItem(
                        getItemBy(itemRequest.itemId),
                        itemRequest.receivedQuantity,
                        itemRequest.unitPurchasePrice,
                        itemRequest.description
                ))
                .toList();
    }

    private Item getItemBy(final Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
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
                BigDecimal unitPurchasePrice,
                String description
        ) {

        }
    }
}
