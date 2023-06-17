package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItemToSplit;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SplitToOutbound {
    private final OutboundRepository outboundRepository;

    public void request(final Request request) {
        final Outbound outbound = outboundRepository.findById(request.outBoundIdToSplit)
                .orElseThrow(() -> new OutboundIdNotFoundException(request.outBoundIdToSplit));

        final Outbound splittedOutbound = outbound.split(
                request.cushioningMaterialQuantity,
                request.listOfOutboundItemToSplit());
    }

    public record Request(
            @NotNull(message = "분할 대상 완충재 수량은 필수 입니다.")
            @Min(value = 0, message = "분할 대상 완충재 수량은 0보다 커야 합니다.")
            Integer cushioningMaterialQuantity,
            @NotNull(message = "분할 대상 출고 ID는 필수 입니다.")
            Long outBoundIdToSplit,
            @NotNull(message = "분할 대상 출고 상품은 필수 입니다.")
            @NotEmpty(message = "분할 대상 출고 상품은 비어 있을 수 없습니다.")
            List<Item> itemsToSplit
    ) {
        List<OutboundItemToSplit> listOfOutboundItemToSplit() {
            return itemsToSplit.stream()
                    .map(item -> new OutboundItemToSplit(
                            item.outboundItemIdToSplit(),
                            item.quantityOfSplit()))
                    .toList();
        }

        public record Item(
                @NotNull(message = "분할 대상 출고 상품 ID는 필수 입니다.")
                Long outboundItemIdToSplit,
                @NotNull(message = "분할 대상 출고 상품 수량은 필수 입니다.")
                @Min(value = 0, message = "분할 대상 출고 상품 수량은 0보다 커야 합니다.")
                Integer quantityOfSplit) {
        }
    }
}
