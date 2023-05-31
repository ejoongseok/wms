package leejoongseok.wms.inbound;

import leejoongseok.wms.item.domain.Item;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class InboundTest {

    //item
    private final long itemId = 1L;
    //inbound
    private final LocalDateTime orderRequestAt = LocalDateTime.now().minusDays(1);
    private final LocalDateTime estimatedArrivalAt = LocalDateTime.now().plusDays(1);
    private final BigDecimal totalAmount = BigDecimal.valueOf(2000);
    //inboundItem
    private final int receivedQuantity = 2;
    private final BigDecimal unitPurchasePrice = BigDecimal.valueOf(1000);

    @Test
    @DisplayName("입고에 입고 상품을 등록한다.")
    void addInboundItems() {
        final Item item = createItem(itemId);
        final Inbound inbound = new Inbound(orderRequestAt, estimatedArrivalAt, totalAmount);
        final InboundItem inboundItem = createInboundItem(item, receivedQuantity, unitPurchasePrice);

        inbound.addInboundItems(List.of(inboundItem));
    }

    private Item createItem(final long itemId) {
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getId), () -> itemId)
                .create();
    }

    private InboundItem createInboundItem(
            final Item item,
            final int receivedQuantity,
            final BigDecimal unitPurchasePrice) {
        return Instancio.of(InboundItem.class)
                .supply(Select.field(InboundItem::getItem), () -> item)
                .supply(Select.field(InboundItem::getReceivedQuantity), () -> receivedQuantity)
                .supply(Select.field(InboundItem::getUnitPurchasePrice), () -> unitPurchasePrice)
                .create();
    }
}