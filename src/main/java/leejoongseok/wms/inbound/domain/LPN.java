package leejoongseok.wms.inbound.domain;

import org.springframework.util.Assert;

import java.time.LocalDateTime;

public class LPN {
    private final String lpnBarcode;
    private final Long itemId;
    private final LocalDateTime expirationAt;
    private final Long inboundItemId;

    public LPN(
            final String lpnBarcode,
            final Long itemId,
            final LocalDateTime expirationAt,
            final Long inboundItemId) {
        Assert.hasText(lpnBarcode, "LPN 바코드는 필수입니다.");
        Assert.notNull(itemId, "아이템 ID는 필수입니다.");
        Assert.notNull(expirationAt, "유통기한은 필수입니다.");
        Assert.notNull(inboundItemId, "입고 아이템 ID는 필수입니다.");
        if (expirationAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("유통기한은 현재 시간보다 나중이어야 합니다.");
        }
        this.lpnBarcode = lpnBarcode;
        this.itemId = itemId;
        this.expirationAt = expirationAt;
        this.inboundItemId = inboundItemId;
    }
}
