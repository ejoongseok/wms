package leejoongseok.wms.outbound.domain;

import lombok.Getter;
import org.springframework.util.Assert;

/**
 * 분할 대상 출고 상품 정보
 * 출고상품 ID
 * 출고 수량
 */
@Getter
public class SplittableOutboundItem {
    private final Long outboundItemId;
    private final Integer quantityToSplit;

    public SplittableOutboundItem(
            final Long outboundItemId,
            final Integer quantityToSplit) {
        validateConstructor(
                outboundItemId,
                quantityToSplit);
        this.outboundItemId = outboundItemId;
        this.quantityToSplit = quantityToSplit;
    }

    private void validateConstructor(
            final Long outboundItemIdToSplit,
            final Integer quantityToSplit) {
        Assert.notNull(outboundItemIdToSplit, "분할 대상 출고 상품의 ID는 필수 입니다.");
        Assert.notNull(quantityToSplit, "분할 대상 출고 상품의 수량은 필수 입니다.");
        Assert.isTrue(0 < quantityToSplit, "분할 대상 출고 상품의 수량은 0보다 커야 합니다.");
    }
}
