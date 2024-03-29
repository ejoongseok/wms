package leejoongseok.wms.location.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import leejoongseok.wms.common.user.BaseEntity;
import leejoongseok.wms.inbound.domain.LPN;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * 로케이션LPN은 로케이션에 적재되어있는 LPN을 의미한다.
 * ex)한 바구니에 사과와 배가 1개씩 들어있다.
 * 바구니(Location) [사과(LPN) 1개, 배(LPN) 1개]
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "location_lpn")
@Comment("로케이션 LPN")
@EqualsAndHashCode(of = "id", callSuper = false)
public class LocationLPN extends BaseEntity {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("로케이션 LPN ID")
    private Long id;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @Comment("로케이션 ID")
    private Location location;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lpn_id", nullable = false)
    @Comment("LPN ID")
    private LPN lpn;
    @Getter
    @Column(name = "inventory_quantity", nullable = false)
    @Comment("재고 수량")
    private Integer inventoryQuantity = 1;
    @Getter
    @Column(name = "item_id", nullable = false)
    @Comment("상품 ID")
    private Long itemId;

    @Version
    @Column(name = "version", nullable = false)
    @Comment("Optimistic Lock을 위한 버전")
    private int version;

    public LocationLPN(
            final Location location,
            final LPN lpn,
            final Long itemId) {
        validateConstructor(
                location,
                lpn,
                itemId);
        this.itemId = itemId;
        this.location = location;
        this.lpn = lpn;
    }

    private void validateConstructor(
            final Location location,
            final LPN lpn,
            final Long itemId) {
        Assert.notNull(location, "로케이션은 필수입니다.");
        Assert.notNull(lpn, "LPN은 필수입니다.");
        Assert.notNull(itemId, "상품 ID는 필수입니다.");
    }

    void incrementInventoryQuantity() {
        inventoryQuantity++;
    }

    public String getLpnBarcode() {
        return lpn.getLpnBarcode();
    }

    void addManualInventoryQuantity(final Integer inventoryQuantity) {
        Assert.notNull(inventoryQuantity, "추가할 재고 수량은 필수입니다.");
        if (0 >= inventoryQuantity)
            throw new IllegalArgumentException("추가할 재고 수량은 1이상이어야 합니다.");

        this.inventoryQuantity += inventoryQuantity;
    }

    /**
     * LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다.
     */
    public boolean isFreshLPNBy(final LocalDateTime thisDateTime) {
        return lpn.isFreshBy(thisDateTime);
    }

    public boolean hasInventory() {
        return 0 < inventoryQuantity;
    }

    public Long getLPNId() {
        return lpn.getId();
    }

    public boolean isPickingAllocatable(final LocalDateTime thisDateTime) {
        return isFreshLPNBy(thisDateTime) &&
                location.isStow()
                && 0L < inventoryQuantity;
    }

    public LocalDateTime getExpirationAt() {
        return lpn.getExpirationAt();
    }

    public String getLocationBarcode() {
        return location.getLocationBarcode();
    }

    public void deductInventory(final Integer quantityRequiredForPick) {
        validateDeductInventory(quantityRequiredForPick);
        inventoryQuantity -= quantityRequiredForPick;
    }

    private void validateDeductInventory(final Integer quantityRequiredForPick) {
        Assert.notNull(quantityRequiredForPick, "차감할 재고 수량은 필수입니다.");
        if (0 >= quantityRequiredForPick) {
            throw new IllegalArgumentException("차감할 재고 수량은 1이상이어야 합니다.");
        }
        if (inventoryQuantity < quantityRequiredForPick) {
            throw new IllegalArgumentException(
                    "차감할 재고 수량이 재고 수량보다 많습니다. 재고 수량: %d, 차감할 재고 수량: %d"
                            .formatted(
                                    inventoryQuantity,
                                    quantityRequiredForPick
                            ));
        }
    }

    /**
     * 재고 수량을 조정한다.
     */
    public void adjustQuantity(final Integer quantity) {
        validateAdjustQuantity(quantity);
        inventoryQuantity = quantity;
    }

    private void validateAdjustQuantity(final Integer quantity) {
        Assert.notNull(quantity, "조정할 재고 수량은 필수입니다.");
        if (0 > quantity) {
            throw new IllegalArgumentException("조정할 재고 수량은 0이상이어야 합니다.");
        }
    }
}
