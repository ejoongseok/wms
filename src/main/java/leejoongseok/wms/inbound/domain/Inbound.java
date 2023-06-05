package leejoongseok.wms.inbound.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import leejoongseok.wms.inbound.exception.InboundItemIdNotFoundException;
import leejoongseok.wms.inbound.exception.UnconfirmedInboundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inbound")
@Comment("입고")
public class Inbound {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("입고 ID")
    private Long id;
    @Column(name = "order_request_at", nullable = false)
    @Comment("발주 요청일시")
    private LocalDateTime orderRequestAt;
    @Column(name = "estimated_arrival_at", nullable = false)
    @Comment("예상 도착일시")
    private LocalDateTime estimatedArrivalAt;
    @Getter
    @Column(name = "total_amount", nullable = false)
    @Comment("입고 총액")
    private BigDecimal totalAmount;
    @OneToMany(mappedBy = "inbound", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<InboundItem> inboundItems = new ArrayList<>();
    @Getter
    @Column(name = "status", nullable = false)
    @Comment("입고 진행 상태")
    @Enumerated(EnumType.STRING)
    private InboundStatus status = InboundStatus.ORDER_REQUESTED;
    @Getter
    @Lob
    @Column(name = "rejection_reasons", nullable = true)
    @Comment("입고 거부 사유")
    private String rejectionReasons;

    public Inbound(
            final LocalDateTime orderRequestAt,
            final LocalDateTime estimatedArrivalAt,
            final BigDecimal totalAmount) {
        final LocalDateTime today = LocalDateTime.now();
        if (null == orderRequestAt || today.isBefore(orderRequestAt)) {
            throw new IllegalArgumentException("발주 요청일시는 현재시간보다 과거여야 합니다.");
        }
        if (null == estimatedArrivalAt || today.isAfter(estimatedArrivalAt)) {
            throw new IllegalArgumentException("예상 도착일시는 현재시간보다 미래여야 합니다.");
        }
        if (null == totalAmount || 0 > totalAmount.intValue()) {
            throw new IllegalArgumentException("총 주문 금액은 0원원 이상이어야 합니다.");
        }
        this.orderRequestAt = orderRequestAt;
        this.estimatedArrivalAt = estimatedArrivalAt;
        this.totalAmount = totalAmount;
    }

    public void addInboundItems(final List<InboundItem> inboundItems) {
        validateInboundItems(inboundItems);
        for (final InboundItem inboundItem : inboundItems) {
            this.inboundItems.add(inboundItem);
            inboundItem.assignInbound(this);
        }
    }

    private void validateInboundItems(
            final List<InboundItem> inboundItems) {
        Assert.notEmpty(inboundItems, "입고 상품은 1개 이상이어야 합니다.");
        final BigDecimal totalPurchasePrice = calculateTotalPurchasePrice(inboundItems);
        if (0 != totalAmount.compareTo(totalPurchasePrice)) {
            throw new IllegalStateException(
                    String.format("입고 상품의 총 금액이 주문 금액과 일치하지 않습니다. " +
                                    "입고총액: %s, 단품 합산액: %s",
                            totalAmount, totalPurchasePrice));
        }
    }

    private BigDecimal calculateTotalPurchasePrice(
            final List<InboundItem> inboundItems) {
        return inboundItems.stream()
                .map(item -> item.getUnitPurchasePrice()
                        .multiply(BigDecimal.valueOf(item.getReceivedQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void confirmInspected() {
        if (InboundStatus.ORDER_REQUESTED != status) {
            throw new IllegalStateException(
                    String.format("입고 확정 할 수 있는 상태가 아닙니다. 현재 상태:[%s]",
                            status.getDescription()));
        }
        status = InboundStatus.CONFIRM_INSPECTED;
    }

    public void reject(final String rejectionReasons) {
        Assert.hasText(rejectionReasons, "입고 거부 사유는 필수입니다.");
        if (InboundStatus.ORDER_REQUESTED != status) {
            throw new IllegalStateException(
                    String.format("입고 거부 할 수 있는 상태가 아닙니다. 현재 상태:[%s]",
                            status.getDescription()));
        }
        status = InboundStatus.REJECTED;
        this.rejectionReasons = rejectionReasons;
    }

    public LPN createLPN(
            final Long inboundItemId,
            final String lpnBarcode,
            final LocalDateTime expirationAt) {
        validateLPNCreation(inboundItemId, lpnBarcode, expirationAt);
        final InboundItem lpnAssignTargetInboundItem = getInboundItemBy(inboundItemId);
        return lpnAssignTargetInboundItem.createLPN(lpnBarcode, expirationAt);
    }

    private void validateLPNCreation(
            final Long inboundItemId,
            final String lpnBarcode,
            final LocalDateTime expirationAt) {
        Assert.notNull(inboundItemId, "입고 상품 ID는 필수입니다.");
        Assert.hasText(lpnBarcode, "LPN 바코드는 필수입니다.");
        Assert.notNull(expirationAt, "유통기한은 필수입니다.");
        if (expirationAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("유통기한은 현재시간보다 미래여야 합니다.");
        }
        if (InboundStatus.CONFIRM_INSPECTED != status) {
            throw new UnconfirmedInboundException(
                    "입고 확인이 완료되지 않은 입고 아이템에는 LPN을 생성할 수 없습니다.");
        }
    }

    private InboundItem getInboundItemBy(final Long inboundItemId) {
        return inboundItems.stream()
                .filter(inboundItem -> inboundItem.getId().equals(inboundItemId))
                .findFirst()
                .orElseThrow(() -> new InboundItemIdNotFoundException(inboundItemId));
    }

}
