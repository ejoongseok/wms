package leejoongseok.wms.outbound.order;

import leejoongseok.wms.item.domain.Item;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * 주문 상품
 */
@Getter
public class OrderItem {
    private final Item item;
    private final Integer orderQuantity;
    private final BigDecimal unitPrice;

    public OrderItem(
            final Item item,
            final Integer orderQuantity,
            final BigDecimal unitPrice) {
        validateConstructor(
                item,
                orderQuantity,
                unitPrice);
        this.item = item;
        this.orderQuantity = orderQuantity;
        this.unitPrice = unitPrice;
    }

    private void validateConstructor(
            final Item item,
            final Integer orderQuantity,
            final BigDecimal unitPrice) {
        Assert.notNull(item, "주문 상품은 필수입니다.");
        Assert.notNull(orderQuantity, "주문 수량은 필수입니다.");
        Assert.notNull(unitPrice, "주문 단가는 필수입니다.");
    }

    public Long getItemId() {
        return item.getId();
    }

    /**
     * 주문 상품의 총 부피를 계산한다.
     */
    public long calculateTotalVolume() {
        return item.calculateVolume() * orderQuantity;
    }

    /**
     * 주문 상품의 총 무게를 계산한다.
     */
    public long calculateTotalWeightInGrams() {
        return (long) item.getWeightInGrams() * orderQuantity;
    }
}
