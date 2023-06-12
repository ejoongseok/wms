package leejoongseok.wms.outbound.domain;

import org.springframework.util.Assert;

import java.math.BigDecimal;

public class OutboundItem {
    private final Long itemId;
    private final Integer orderQuantity;
    private final BigDecimal unitPrice;
    private Outbound outbound;

    public OutboundItem(
            final Long itemId,
            final Integer orderQuantity,
            final BigDecimal unitPrice) {
        Assert.notNull(itemId, "상품 ID는 필수입니다.");
        Assert.notNull(orderQuantity, "출고 수량은 필수입니다.");
        Assert.notNull(unitPrice, "출고 단가는 필수입니다.");
        if (0 >= orderQuantity) {
            throw new IllegalArgumentException("출고 수량은 0보다 커야합니다.");
        }
        if (0 > unitPrice.compareTo(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("출고 단가는 0이상이어야 합니다.");
        }
        this.itemId = itemId;
        this.orderQuantity = orderQuantity;
        this.unitPrice = unitPrice;


    }

    public void assignOutbound(final Outbound outbound) {
        Assert.notNull(outbound, "출고는 필수입니다.");
        this.outbound = outbound;
    }
}
