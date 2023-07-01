package leejoongseok.wms.location.domain;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.location.exception.LocationLPNNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 로케이션은 상품의 LPN을 보관하는 장소이다.
 * 로케이션은 셀, 렉, 토트, 파레트 등이 될 수 있다.
 * 셀과 렉은 물리적 이동이 거의 없이 고정되어 있고,
 * 토트와 파레트는 물리적 이동이 잦다.
 */
@Entity
@Table(name = "location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("로케이션")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("로케이션 ID")
    private Long id;
    @Getter
    @Column(name = "location_barcode", nullable = false)
    @Comment("로케이션 바코드")
    private String locationBarcode;
    @Getter
    @Column(name = "storage_type", nullable = false)
    @Comment("보관 타입")
    @Enumerated(EnumType.STRING)
    private StorageType storageType;
    @Getter
    @Column(name = "usage_purpose", nullable = false)
    @Comment("사용 목적")
    @Enumerated(EnumType.STRING)
    private UsagePurpose usagePurpose;
    @Getter
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<LocationLPN> locationLPNList = new ArrayList<>();

    public Location(
            final String locationBarcode,
            final StorageType storageType,
            final UsagePurpose usagePurpose) {
        validateConstructor(
                locationBarcode,
                storageType,
                usagePurpose);
        this.locationBarcode = locationBarcode;
        this.storageType = storageType;
        this.usagePurpose = usagePurpose;
    }

    private void validateConstructor(
            final String locationBarcode,
            final StorageType storageType,
            final UsagePurpose usagePurpose) {
        Assert.hasText(locationBarcode, "로케이션 바코드는 필수입니다.");
        Assert.notNull(storageType, "보관 타입은 필수입니다.");
        Assert.notNull(usagePurpose, "보관 목적은 필수입니다.");
    }

    /**
     * 로케이션에 LPN을 적재시키는 행위를 수행한다.
     * 로케이션에 LPN이 이미 존재하는경우 LocationLPN의 inventory quantity만 증가시킨다.
     * 로케이션에 LPN이 존재하지 않으면 LocationLPN을 새로 생성해서 등록한다.
     * 새로 등록한 LocationLPN은 재고수량이 1이고 Location의 locationLPNList에 추가된다.
     */
    public void assignLPN(final LPN lpn) {
        Assert.notNull(lpn, "LPN은 필수입니다.");
        findLocationLPNBy(lpn)
                .ifPresentOrElse(
                        LocationLPN::incrementInventoryQuantity,
                        () -> assignNewLocationLPN(lpn));
    }

    private Optional<LocationLPN> findLocationLPNBy(final LPN lpn) {
        return locationLPNList.stream()
                .filter(locationLPN -> lpn.equals(locationLPN.getLpn()))
                .findFirst();
    }

    private void assignNewLocationLPN(final LPN lpn) {
        locationLPNList.add(new LocationLPN(
                this,
                lpn,
                lpn.getItemId()));
    }

    @VisibleForTesting
    LocationLPN testingGetLocationLPN(final String lpnBarcode) {
        return locationLPNList.stream()
                .filter(locationLPN -> lpnBarcode.equals(
                        locationLPN.getLpnBarcode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 LPN이 존재하지 않습니다."));
    }

    /**
     * 로케이션에 LPN의 바코드를 하나씩 스캔하면서 재고를 적재하는 행위(assignLPN)가 아닌
     * 시스템에 재고를 증가시킬 숫자를 직접 입력해서 한번에 재고를 추가하는 기능이다.
     * 추가해야할 재고가 너무 많아서 하나씩 스캔하는 것이 비효율적인 경우에 사용한다.
     */
    public void addManualInventoryToLocationLPN(
            final LPN lpn,
            final Integer inventoryQuantity) {
        validateManualInventoryParameter(lpn, inventoryQuantity);
        final LocationLPN locationLPN = getLocationLPN(lpn);
        locationLPN.addManualInventoryQuantity(inventoryQuantity);
    }

    private void validateManualInventoryParameter(
            final LPN lpn,
            final Integer inventoryQuantity) {
        Assert.notNull(lpn, "재고 수량을 추가할 LPN은 필수입니다.");
        Assert.notNull(inventoryQuantity, "추가할 재고 수량은 필수입니다.");
        if (0 >= inventoryQuantity) {
            throw new IllegalArgumentException("추가할 재고 수량은 1이상이어야 합니다.");
        }
    }

    private LocationLPN getLocationLPN(final LPN lpn) {
        return findLocationLPNBy(lpn)
                .orElseThrow(() -> new LocationLPNNotFoundException(
                        "LocationLPN을 찾을 수 없습니다."));
    }


    public boolean isTote() {
        return StorageType.TOTE == storageType;
    }

    public boolean hasLocationLPN() {
        return !locationLPNList.isEmpty() && locationLPNList.stream().anyMatch(
                locationLPN -> !locationLPN.isEmptyInventory());
    }

    public boolean isStow() {
        return UsagePurpose.STOW == usagePurpose;
    }

    public void addChildLocation(final Location location) {
        Assert.notNull(location, "로케이션은 필수입니다.");
    }
}
