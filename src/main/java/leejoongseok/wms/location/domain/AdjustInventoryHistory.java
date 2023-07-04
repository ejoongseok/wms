package leejoongseok.wms.location.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Table(name = "adjust_inventory_history")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdjustInventoryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "location_lpn_id", nullable = false)
    @Comment("로케이션 LPN ID")
    private Long locationLPNId;
    @Column(name = "location_barcode", nullable = false)
    @Comment("로케이션 바코드")
    private String locationBarcode;
    @Column(name = "lpn_barcode", nullable = false)
    @Comment("LPN 바코드")
    private String lpnBarcode;
    @Column(name = "before_quantity", nullable = false)
    @Comment("재고 변경 전 수량")
    private Integer beforeQuantity;
    @Column(name = "after_quantity", nullable = false)
    @Comment("재고 변경 후 수량")
    private Integer afterQuantity;
    @Column(name = "reason", nullable = false)
    @Comment("재고 변경 이유")
    private String reason;
    @Column(name = "adjusted_at", nullable = false, updatable = false)
    @Comment("재고 변경 일시")
    private final LocalDateTime adjustedAt = LocalDateTime.now();

    @Column(name = "adjusted_user_id", nullable = false, updatable = false)
    @Comment("재고 변경 사용자 ID")
    @CreatedBy
    private Long adjusted_user_id;

    public AdjustInventoryHistory(
            final Long locationLPNId,
            final String locationBarcode,
            final String lpnBarcode,
            final Integer beforeQuantity,
            final Integer afterQuantity,
            final String reason) {
        validateConstructor(
                locationLPNId,
                locationBarcode,
                lpnBarcode,
                afterQuantity,
                beforeQuantity,
                reason);
        this.locationLPNId = locationLPNId;
        this.locationBarcode = locationBarcode;
        this.lpnBarcode = lpnBarcode;
        this.afterQuantity = afterQuantity;
        this.beforeQuantity = beforeQuantity;
        this.reason = reason;

    }

    private void validateConstructor(
            final Long locationLPNId,
            final String locationBarcode,
            final String lpnBarcode,
            final Integer afterQuantity,
            final Integer beforeQuantity,
            final String reason) {
        Assert.notNull(locationLPNId, "locationLPN ID는 필수입니다.");
        Assert.hasText(locationBarcode, "로케이션 바코드는 필수입니다.");
        Assert.hasText(lpnBarcode, "LPN 바코드는 필수입니다.");
        Assert.notNull(afterQuantity, "재고 변경 후 수량은 필수입니다.");
        if (0 > afterQuantity) {
            throw new IllegalArgumentException("재고 변경 후 수량은 0보다 작을 수 없습니다.");
        }
        Assert.notNull(beforeQuantity, "재고 변경 전 수량은 필수입니다.");
        if (0 > beforeQuantity) {
            throw new IllegalArgumentException("재고 변경 전 수량은 0보다 작을 수 없습니다.");
        }
        Assert.hasText(reason, "재고 변경 이유는 필수입니다.");
    }
}
