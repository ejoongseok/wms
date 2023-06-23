package leejoongseok.wms.outbound.domain;

import lombok.Getter;
import org.springframework.util.Assert;

/**
 * 분할 대상 출고 상품 정보
 * 출고상품 ID
 * 출고 수량
 */
@Getter
public class OutboundItemToSplit {
    private final Long outboundItemId;
    private final Integer quantityOfSplit;

    public OutboundItemToSplit(
            final Long outboundItemId,
            final Integer quantityOfSplit) {
        validateConstructor(
                outboundItemId,
                quantityOfSplit);
        this.outboundItemId = outboundItemId;
        this.quantityOfSplit = quantityOfSplit;
    }

    private void validateConstructor(
            final Long outboundItemIdToSplit,
            final Integer quantityOfSplit) {
        Assert.notNull(outboundItemIdToSplit, "분할 대상 출고 상품의 ID는 필수 입니다.");
        Assert.notNull(quantityOfSplit, "분할 대상 출고 상품의 수량은 필수 입니다.");
        Assert.isTrue(0 < quantityOfSplit, "분할 대상 출고 상품의 수량은 0보다 커야 합니다.");
    }
}
