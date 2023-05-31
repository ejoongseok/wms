package leejoongseok.wms.inbound;

import leejoongseok.wms.item.domain.Item;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class InboundTest {

    @Test
    @DisplayName("입고에 입고 상품을 등록한다.")
    void addInboundItems() {
        final Item item = createItem(1L);
        final Inbound inbound = Instancio.create(Inbound.class);
        final InboundItem inboundItem = createInboundItem(item);

        inbound.addInboundItems(List.of(inboundItem));


    }

    private Item createItem(final long itemId) {
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getId), () -> itemId)
                .create();
    }

    private InboundItem createInboundItem(final Item item) {
        return Instancio.of(InboundItem.class)
                .supply(Select.field(InboundItem::getItem), () -> item)
                .create();
    }
}