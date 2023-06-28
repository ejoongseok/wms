package leejoongseok.wms.outbound.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import leejoongseok.wms.location.domain.LocationLPN;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

@Entity
@Table(name = "picking")
@Comment("집품")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Picking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_lpn_id", nullable = false)
    @Comment("로케이션 LPN ID")
    private LocationLPN locationLPN;
    @Getter
    @Column(name = "status", nullable = false)
    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private PickingStatus status = PickingStatus.READY;
    @Getter
    @Column(name = "picked_quantity", nullable = false)
    @Comment("집품한 수량")
    private Integer pickedQuantity = 0;
    @Getter
    @Column(name = "quantity_required_for_pick", nullable = false)
    @Comment("집품해야할 수량")
    private Integer quantityRequiredForPick = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("출고 상품")
    @JoinColumn(name = "outbound_item_id")
    private OutboundItem outboundItem;

    public Picking(
            final LocationLPN locationLPN,
            final Integer quantityRequiredForPick) {
        Assert.notNull(locationLPN, "로케이션 LPN은 필수입니다.");
        Assert.isTrue(1 <= quantityRequiredForPick, "집품할 수량은 1개 이상이어야 합니다.");
        this.locationLPN = locationLPN;
        this.quantityRequiredForPick = quantityRequiredForPick;
    }

    public void assignOutboundItem(final OutboundItem outboundItem) {
        Assert.notNull(outboundItem, "출고 상품은 필수입니다.");
        this.outboundItem = outboundItem;
    }

    public boolean hasPickedItem() {
        return 0 < pickedQuantity;
    }

    public void deductAllocatedInventory() {
        if (PickingStatus.READY != status) {
            throw new IllegalStateException(
                    "집품에 할당된 LocationLPN의 재고를 차감하기위해서는 " +
                            "집품을 시작하기 전이어야 합니다.");
        }
        locationLPN.deductInventory(quantityRequiredForPick);
    }

    public void increasePickedQuantity(final LocationLPN locationLPN) {
        validateIncreasePickedQuantity(locationLPN);
        pickedQuantity++;
        status = PickingStatus.IN_PROGRESS;
        if (pickedQuantity == quantityRequiredForPick) {
            status = PickingStatus.COMPLETED;
        }
    }

    private void validateIncreasePickedQuantity(final LocationLPN locationLPN) {
        Assert.notNull(locationLPN, "로케이션 LPN은 필수입니다.");
        if (PickingStatus.COMPLETED == status) {
            throw new IllegalStateException(
                    "이미 완료된 집품은 집품 수량을 증가시킬 수 없습니다.");
        }
        if (!this.locationLPN.equals(locationLPN)) {
            throw new IllegalArgumentException(
                    ("집품에 할당된 LocationLPN이 아닌 LocationLPN의 수량을 증가시킬 수 없습니다. " +
                            "집품에 할당된 LocationBarcode: %s, LPNBarcode: %s " +
                            "스캔한 LocationBarcode: %s, LPNBarcode: %s").formatted(
                            this.locationLPN.getLocationBarcode(),
                            this.locationLPN.getLpnBarcode(),
                            locationLPN.getLocationBarcode(),
                            locationLPN.getLpnBarcode()));
        }
        if (quantityRequiredForPick <= pickedQuantity) {
            throw new IllegalStateException(
                    "집품해야할 수량보다 집품하려는 수량이 많습니다. " +
                            "집품해야할 수량: " + quantityRequiredForPick + ", " +
                            "집품한 수량: " + pickedQuantity);
        }
    }

    public boolean isInProgress() {
        return PickingStatus.IN_PROGRESS == status;
    }

    public boolean isCompletedPicking() {
        return PickingStatus.COMPLETED == status;
    }
}
