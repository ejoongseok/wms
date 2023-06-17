package leejoongseok.wms.outbound.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * 출고 상품은 출고에 포함되는 상품을 의미합니다.
 */
@Entity
@Table(name = "outbound_item")
@Comment("출고 상품")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboundItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("출고 상품 ID")
    private Long id;
    @Column(name = "item_id", nullable = false)
    @Comment("상품 ID")
    private Long itemId;
    @Column(name = "outbound_quantity", nullable = false)
    @Comment("출고 수량")
    private Integer outboundQuantity;
    @Column(name = "unit_price", nullable = false)
    @Comment("출고 단가")
    private BigDecimal unitPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outbound_id")
    @Comment("출고")
    private Outbound outbound;

    public OutboundItem(
            final Long itemId,
            final Integer outboundQuantity,
            final BigDecimal unitPrice) {
        validateConstructor(
                itemId,
                outboundQuantity,
                unitPrice);
        this.itemId = itemId;
        this.outboundQuantity = outboundQuantity;
        this.unitPrice = unitPrice;
    }

    private void validateConstructor(
            final Long itemId,
            final Integer outboundQuantity,
            final BigDecimal unitPrice) {
        Assert.notNull(itemId, "상품 ID는 필수입니다.");
        Assert.notNull(outboundQuantity, "출고 수량은 필수입니다.");
        Assert.notNull(unitPrice, "출고 단가는 필수입니다.");
        if (0 >= outboundQuantity) {
            throw new IllegalArgumentException("출고 수량은 0보다 커야합니다.");
        }
        if (0 > unitPrice.compareTo(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("출고 단가는 0이상이어야 합니다.");
        }
    }

    public void assignOutbound(final Outbound outbound) {
        Assert.notNull(outbound, "출고는 필수입니다.");
        this.outbound = outbound;
    }
}
