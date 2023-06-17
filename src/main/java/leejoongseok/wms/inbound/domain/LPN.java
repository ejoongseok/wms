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

/**
 * 본 프로젝트는 입고(Inbound) 상품(InboundItem) 중 유통기한이 같은 상품들의 논리적인 집합을 LPN이라고 정의한다.
 * 단, 같은 상품의 같은 유통기한을 가진 상품이 여러번 들어오더라도 입고(InboundId)및 입고상품(InboundItemId)이 다르면 LPN이 다르다.
 * Inbound(1) -> InboundItem(n) -> LPN(n)
 * A입고의 입고상품에 유통기한만 다른 상품이 2개 들어온 경우
 * 입고상품에 대한 LPN은 2개가 된다.
 * 전산상 입고를 등록할때는 입고상품의 유통기한을 입력하지 않는다.(발주를 넣은 입고상품에 유통기한이 같은지 다른지 알 수 없기 때문이다.)
 * 유통기한을 확인하는건 실물 상품을 입고하고 분류할 때이다.
 */
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
        validateConstructor(
                lpnBarcode,
                itemId,
                expirationAt,
                inboundItemId,
                createdAt);
        this.lpnBarcode = lpnBarcode;
        this.itemId = itemId;
        this.expirationAt = expirationAt;
        this.inboundItemId = inboundItemId;
    }

    private void validateConstructor(
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
    }

    /**
     * LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다.
     */
    public boolean isFreshBy(final LocalDateTime thisDateTime) {
        return expirationAt.isAfter(thisDateTime);
    }
}
