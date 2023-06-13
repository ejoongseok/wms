package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.item.domain.Item;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItem {
    private final Item item;
    private final Integer orderQuantity;
    private final BigDecimal unitPrice;

    public OrderItem(final Item item, final Integer orderQuantity, final BigDecimal unitPrice) {
        this.item = item;
        this.orderQuantity = orderQuantity;
        this.unitPrice = unitPrice;
    }

    public Long getItemId() {
        return item.getId();
    }

    public long calculateVolume() {
        return item.calculateVolume() * orderQuantity;
    }

    public long calculateWeightInGrams() {
        return (long) item.getWeightInGrams() * orderQuantity;
    }
}
