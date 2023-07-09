package leejoongseok.wms.common.fixture;

import leejoongseok.wms.inbound.domain.InboundItem;
import leejoongseok.wms.item.domain.Item;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

import java.math.BigDecimal;

public class InboundItemFixture {

    private InstancioApi<InboundItem> inboundItemInstance = Instancio.of(InboundItem.class);

    public static InboundItemFixture aInboundItem() {
        return new InboundItemFixture();
    }

    public static InboundItemFixture aDefaultInboundItem() {
        return aInboundItem()
                .withId(1L)
                .withReceivedQuantity(2)
                .withUnitPurchasePrice(BigDecimal.valueOf(1000));
    }

    public InboundItemFixture withId(final Long id) {
        inboundItemInstance.supply(Select.field(InboundItem::getId), () -> id);
        return this;
    }

    public InboundItemFixture withItem(final Item item) {
        inboundItemInstance.supply(Select.field(InboundItem::getItem), () -> item);
        return this;
    }

    public InboundItemFixture withReceivedQuantity(final Integer receivedQuantity) {
        inboundItemInstance.supply(Select.field(InboundItem::getReceivedQuantity), () -> receivedQuantity);
        return this;
    }

    public InboundItemFixture withUnitPurchasePrice(final BigDecimal unitPurchasePrice) {
        inboundItemInstance.supply(Select.field(InboundItem::getUnitPurchasePrice), () -> unitPurchasePrice);
        return this;
    }

    public InboundItem build() {
        inboundItemInstance = null == inboundItemInstance ? Instancio.of(InboundItem.class) : inboundItemInstance;
        return inboundItemInstance.create();
    }
}
