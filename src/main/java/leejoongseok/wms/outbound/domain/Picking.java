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
    @Column(name = "quantity_required_for_pick", nullable = false)
    @Comment("집품해야할 수량")
    private Integer quantityRequiredForPick = 0;
    @Column(name = "picked_quantity", nullable = false)
    @Comment("집품한 수량")
    private final Integer pickedQuantity = 0;
    @Column(name = "status", nullable = false)
    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private final PickingStatus status = PickingStatus.READY;
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
}
