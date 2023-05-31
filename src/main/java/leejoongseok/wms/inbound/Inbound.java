package leejoongseok.wms.inbound;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Inbound {
    @Getter
    private Long id;
    private final LocalDateTime orderRequestAt;
    private final LocalDateTime estimatedArrivalAt;
    private final BigDecimal totalAmount;

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
