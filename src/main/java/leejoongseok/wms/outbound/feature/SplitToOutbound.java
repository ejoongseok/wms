package leejoongseok.wms.outbound.feature;

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

        final Outbound splittedOutbound = outbound.split(request.listOfOutboundItemToSplit());
    }

    public record Request(
            Long outBoundIdToSplit,
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
                Long outboundItemIdToSplit,
                Integer quantityOfSplit) {
        }
    }
}
