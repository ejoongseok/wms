package leejoongseok.wms.outbound.feature;

import java.util.List;

public class SplitToOutbound {
    public void request(final Request request) {

    }

    public record Request(
            Long outBoundIdToSplit,
            List<Item> itemsToSplit
    ) {

        public record Item(
                Long outboundItemIdToSplit,
                int quantityOfSplit) {
        }
    }
}
