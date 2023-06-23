package leejoongseok.wms.inbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundItem;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 입고 요청을 생성하는 기능을 수행하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
public class CreateInbound {
    private final InboundRepository inboundRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @PostMapping("/inbounds")
    @ResponseStatus(HttpStatus.CREATED)
    public void request(@RequestBody @Valid final Request request) {
        final Inbound inbound = request.toEntity();
        final List<InboundItem> inboundItems = toInboundItems(
                request.itemRequests);
        inbound.addInboundItems(inboundItems);
        inboundRepository.save(inbound);
    }

    private List<InboundItem> toInboundItems(
            final List<Request.ItemRequest> itemRequests) {
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
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 상품입니다."));
    }

    public record Request(
            @Past(message = "주문 요청일시는 현재시간보다 과거여야 합니다.")
            @NotNull(message = "주문 요청일은 필수입니다.")
            LocalDateTime orderRequestAt,
            @Future(message = "예상 도착일시는 현재시간보다 미래여야 합니다.")
            @NotNull(message = "예상 도착일시는 필수입니다.")
            LocalDateTime estimatedArrivalAt,
            @PositiveOrZero(message = "총 주문 금액은 0원 이상이어야 합니다.")
            @NotNull(message = "총 주문 금액은 필수입니다.")
            BigDecimal totalAmount,
            @NotEmpty(message = "입고할 상품목록은 필수입니다.")
            List<ItemRequest> itemRequests
    ) {
        Inbound toEntity() {
            return new Inbound(
                    orderRequestAt,
                    estimatedArrivalAt,
                    totalAmount
            );
        }

        public record ItemRequest(
                @NotNull(message = "상품 ID는 필수입니다.")
                Long itemId,
                @Positive(message = "입고 수량은 1개 이상이어야 합니다.")
                Integer receivedQuantity,
                @PositiveOrZero(message = "입고 단가는 0원 이상이어야 합니다.")
                BigDecimal unitPurchasePrice,
                String description
        ) {

        }
    }
}
