package leejoongseok.wms.common.fixture;

import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.outbound.order.OrderItem;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class OrderItemFixture {

    private InstancioApi<OrderItem> orderItemInstance = Instancio.of(OrderItem.class);

    public static OrderItemFixture aOrderItem() {
        return new OrderItemFixture();
    }

    public OrderItemFixture withItem(final Item item) {
        orderItemInstance.supply(Select.field(OrderItem::getItem), () -> item);
        return this;
    }

    public OrderItemFixture withOrderQuantity(final Integer orderQuantity) {
        orderItemInstance.supply(Select.field(OrderItem::getOrderQuantity), () -> orderQuantity);
        return this;
    }

    public OrderItem build() {
        orderItemInstance = null == orderItemInstance ? Instancio.of(OrderItem.class) : orderItemInstance;
        return orderItemInstance.create();
    }

}
