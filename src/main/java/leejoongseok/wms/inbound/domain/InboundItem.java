package leejoongseok.wms.inbound.domain;

import jakarta.persistence.*;
import leejoongseok.wms.common.user.BaseEntity;
import leejoongseok.wms.item.domain.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inbound_item")
@Comment("입고 상품")
@Getter
public class InboundItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("입고 상품 ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @Comment("상품 ID")
    private Item item;
    @Column(name = "received_quantity", nullable = false)
    @Comment("입고 수량")
    private Integer receivedQuantity;
    @Column(name = "unit_purchase_price", nullable = false)
    @Comment("개별 구매 가격")
    private BigDecimal unitPurchasePrice;
    @Column(name = "description")
    @Comment("설명")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inbound_id")
    @Comment("입고 ID")
    private Inbound inbound;

    public InboundItem(
            final Item item,
            final Integer receivedQuantity,
            final BigDecimal unitPurchasePrice,
            final String description) {
        validateConstructor(
                item,
                receivedQuantity,
                unitPurchasePrice);
        this.item = item;
        this.receivedQuantity = receivedQuantity;
        this.unitPurchasePrice = unitPurchasePrice;
        this.description = description;
    }

    private void validateConstructor(
            final Item item,
            final Integer receivedQuantity,
            final BigDecimal unitPurchasePrice) {
        Assert.notNull(item, "상품은 필수입니다.");
        if (null == receivedQuantity || 0 >= receivedQuantity) {
            throw new IllegalArgumentException(
                    "입고 수량은 1개 이상이어야 합니다.");
        }
        if (null == unitPurchasePrice || 0 > unitPurchasePrice.intValue()) {
            throw new IllegalArgumentException(
                    "구매 가격은 0원 이상이어야 합니다.");
        }
    }

    void assignInbound(final Inbound inbound) {
        Assert.notNull(inbound, "입고는 필수입니다.");
        this.inbound = inbound;
    }

    public LPN createLPN(
            final String lpnBarcode,
            final LocalDateTime expirationAt) {
        final LocalDateTime createdAt = LocalDateTime.now();
        validateLPNCreation(
                lpnBarcode,
                expirationAt,
                createdAt);
        return new LPN(
                lpnBarcode,
                item.getId(),
                expirationAt,
                id,
                createdAt
        );
    }

    private void validateLPNCreation(
            final String lpnBarcode,
            final LocalDateTime expirationAt,
            final LocalDateTime createdAt) {
        Assert.notNull(lpnBarcode, "LPN 바코드는 필수입니다.");
        Assert.notNull(expirationAt, "유통기한은 필수입니다.");
        if (expirationAt.isBefore(createdAt)) {
            throw new IllegalArgumentException(
                    "유통기한은 현재시간보다 미래여야 합니다.");
        }
    }
}
