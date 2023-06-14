package leejoongseok.wms.inbound.domain;

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
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "lpn")
@Comment("LPN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LPN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("LPN ID")
    private Long id;
    @Column(name = "lpn_barcode", nullable = false, unique = true)
    @Comment("LPN 바코드 (중복 불가)")
    private String lpnBarcode;
    @Column(name = "item_id", nullable = false)
    @Comment("상품 ID")
    private Long itemId;
    @Column(name = "expiration_at", nullable = false)
    @Comment("유통기한")
    private LocalDateTime expirationAt;
    @Column(name = "inbound_item_id", nullable = false)
    @Comment("입고 아이템 ID")
    private Long inboundItemId;

    public LPN(
            final String lpnBarcode,
            final Long itemId,
            final LocalDateTime expirationAt,
            final Long inboundItemId,
            final LocalDateTime createdAt) {
        Assert.hasText(lpnBarcode, "LPN 바코드는 필수입니다.");
        Assert.notNull(itemId, "아이템 ID는 필수입니다.");
        Assert.notNull(expirationAt, "유통기한은 필수입니다.");
        Assert.notNull(inboundItemId, "입고 아이템 ID는 필수입니다.");
        if (expirationAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("유통기한은 생성 시간보다 나중이어야 합니다.");
        }
        this.lpnBarcode = lpnBarcode;
        this.itemId = itemId;
        this.expirationAt = expirationAt;
        this.inboundItemId = inboundItemId;
    }

    /**
     * LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다.
     */
    public boolean isFreshBy(final LocalDateTime thisDateTime) {
        return expirationAt.isAfter(thisDateTime);
    }
}
