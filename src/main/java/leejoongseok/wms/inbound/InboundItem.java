package leejoongseok.wms.inbound;

import leejoongseok.wms.item.domain.Item;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class InboundItem {
    @Getter
    private final Item item;
    private final Integer receivedQuantity;
    private final BigDecimal unitPurchasePrice;
    private final String description;

    public InboundItem(
            final Item item,
            final Integer receivedQuantity,
            final BigDecimal unitPurchasePrice,
            final String description) {
        Assert.notNull(item, "상품은 필수입니다.");
        if (null == receivedQuantity || 0 >= receivedQuantity) {
            throw new IllegalArgumentException("입고 수량은 1개 이상이어야 합니다.");
        }
        if (null == unitPurchasePrice || 0 >= unitPurchasePrice.compareTo(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("구매 가격은 0원보다 커야 합니다.");
        }
        this.item = item;
        this.receivedQuantity = receivedQuantity;
        this.unitPurchasePrice = unitPurchasePrice;
        this.description = description;
    }
}
