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
import leejoongseok.wms.inbound.domain.LPN;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "location_lpn")
@Comment("로케이션 LPN")
public class LocationLPN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("로케이션 LPN ID")
    private Long id;
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

    public LocationLPN(final Location location, final LPN lpn) {
        Assert.notNull(location, "로케이션은 필수입니다.");
        Assert.notNull(lpn, "LPN은 필수입니다.");
        this.location = location;
        this.lpn = lpn;
    }

    public void increaseInventoryQuantity() {
        inventoryQuantity++;
    }

    public String getLpnBarcode() {
        return lpn.getLpnBarcode();
    }
}
