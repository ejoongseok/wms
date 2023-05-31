package leejoongseok.wms.inbound;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @Column(name = "total_amount", nullable = false)
    @Comment("입고 총액")
    private BigDecimal totalAmount;

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
        if (null == totalAmount || 0 > totalAmount.compareTo(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("총 주문 금액은 0원보다 커야 합니다.");
        }
        this.orderRequestAt = orderRequestAt;
        this.estimatedArrivalAt = estimatedArrivalAt;
        this.totalAmount = totalAmount;
    }

    public void assignId(final Long id) {
        this.id = id;
    }
}
