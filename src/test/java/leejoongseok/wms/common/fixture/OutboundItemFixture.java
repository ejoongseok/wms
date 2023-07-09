package leejoongseok.wms.common.fixture;

import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.Picking;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

import java.util.List;

public class OutboundItemFixture {

    private InstancioApi<OutboundItem> outboundItemInstance = Instancio.of(OutboundItem.class);

    public static OutboundItemFixture aOutboundItem() {
        return new OutboundItemFixture();
    }

    public static OutboundItemFixture aOutboundItemWithNoPickings() {
        return aOutboundItem()
                .ignorePickings();
    }

    public OutboundItemFixture withItem(final Item item) {
        outboundItemInstance.supply(Select.field(OutboundItem::getItem), () -> item);
        return this;
    }

    public OutboundItemFixture withOutboundQuantity(final Integer outboundQuantity) {
        outboundItemInstance.supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundQuantity);
        return this;
    }

    public OutboundItemFixture withPickings(final List<Picking> pickings) {
        outboundItemInstance.supply(Select.field(OutboundItem::getPickings), () -> pickings);
        return this;
    }

    public OutboundItemFixture withId(final Long id) {
        outboundItemInstance.supply(Select.field(OutboundItem::getId), () -> id);
        return this;
    }

    public OutboundItemFixture ignorePickings() {
        outboundItemInstance.ignore(Select.field(OutboundItem::getPickings));
        return this;
    }

    public OutboundItem build() {
        outboundItemInstance = null == outboundItemInstance ? Instancio.of(OutboundItem.class) : outboundItemInstance;
        return outboundItemInstance.create();
    }
}
