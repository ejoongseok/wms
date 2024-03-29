package leejoongseok.wms.outbound.domain;

import jakarta.persistence.*;
import leejoongseok.wms.common.user.BaseEntity;
import leejoongseok.wms.item.domain.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 출고 상품은 출고에 포함되는 상품을 의미합니다.
 */
@Entity
@Table(name = "outbound_item")
@Comment("출고 상품")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboundItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("출고 상품 ID")
    @Getter
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @Comment("입고 ID")
    @Getter
    private Item item;
    @Column(name = "outbound_quantity", nullable = false)
    @Comment("출고 수량")
    @Getter
    private Integer outboundQuantity;
    @Column(name = "unit_price", nullable = false)
    @Comment("출고 단가")
    @Getter(AccessLevel.PROTECTED)
    private BigDecimal unitPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outbound_id")
    @Comment("출고")
    private Outbound outbound;
    @Getter
    @OneToMany(mappedBy = "outboundItem", cascade = CascadeType.ALL)
    private final List<Picking> pickings = new ArrayList<>();

    public OutboundItem(
            final Item item,
            final Integer outboundQuantity,
            final BigDecimal unitPrice) {
        validateConstructor(
                item,
                outboundQuantity,
                unitPrice);
        this.item = item;
        this.outboundQuantity = outboundQuantity;
        this.unitPrice = unitPrice;
    }

    private void validateConstructor(
            final Item item,
            final Integer outboundQuantity,
            final BigDecimal unitPrice) {
        Assert.notNull(item, "상품은 필수입니다.");
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

    /**
     * 출고 상품을 분할합니다.
     */
    public OutboundItem split(final Integer quantityToSplit) {
        validateSplit(quantityToSplit);
        return new OutboundItem(
                item,
                quantityToSplit,
                unitPrice);
    }

    private void validateSplit(final Integer quantityToSplit) {
        Assert.notNull(quantityToSplit, "분할 수량은 필수입니다.");
        if (quantityToSplit > outboundQuantity) {
            throw new IllegalArgumentException(
                    ("분할 수량은 출고 수량보다 작거나 같아야 합니다. " +
                            "출고 수량: %d, 분할 수량: %d")
                            .formatted(outboundQuantity, quantityToSplit));
        }
    }

    public boolean isZeroQuantity() {
        return 0 == outboundQuantity;
    }

    public Long calculateVolume() {
        return item.calculateVolume() * outboundQuantity;
    }

    public Long calculateWeightInGrams() {
        return (long) item.getWeightInGrams() * outboundQuantity;
    }

    void decreaseQuantity(final Integer quantity) {
        validateDecreaseQuantity(quantity);
        outboundQuantity -= quantity;
    }

    private void validateDecreaseQuantity(final Integer quantity) {
        Assert.notNull(quantity, "감소 수량은 필수입니다.");
        if (0 >= quantity) {
            throw new IllegalArgumentException(
                    "감소 수량은 0보다 커야합니다.");
        }
        if (quantity > outboundQuantity) {
            throw new IllegalArgumentException(
                    ("감소할 수량은 출고 수량보다 작거나 같아야 합니다. " +
                            "출고 수량: %d, 감소 수량: %d")
                            .formatted(outboundQuantity, quantity));
        }
    }

    public Long getItemId() {
        return item.getId();
    }

    public void assignPickings(final List<Picking> pickings) {
        Assert.notEmpty(pickings, "출고 상품에 할당할 집품 목록은 필수입니다.");
        pickings.forEach(picking -> assignPicking(picking));
    }

    private void assignPicking(final Picking picking) {
        Assert.notNull(picking, "출고 상품에 할당할 집품은 필수입니다.");
        picking.assignOutboundItem(this);
        pickings.add(picking);
    }

    public void deductAllocatedInventory() {
        Assert.notEmpty(pickings, "집품 목록이 비어있습니다.");
        for (final Picking picking : pickings) {
            picking.deductAllocatedInventory();
        }
    }

    public boolean isCompletedPicking() {
        return pickings.stream()
                .allMatch(Picking::isCompletedPicking);
    }

    public void resetPickings() {
        pickings.clear();
    }
}
