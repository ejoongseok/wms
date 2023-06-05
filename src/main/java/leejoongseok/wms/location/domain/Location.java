package leejoongseok.wms.location.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

@Entity
@Table(name = "location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("로케이션")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("로케이션 ID")
    private Long id;
    @Column(name = "location_barcode", nullable = false)
    @Comment("로케이션 바코드")
    private String locationBarcode;
    @Column(name = "storage_type", nullable = false)
    @Comment("보관 타입")
    @Enumerated(EnumType.STRING)
    private StorageType storageType;
    @Column(name = "usage_purpose", nullable = false)
    @Comment("사용 목적")
    @Enumerated(EnumType.STRING)
    private UsagePurpose usagePurpose;

    public Location(
            final String locationBarcode,
            final StorageType storageType,
            final UsagePurpose usagePurpose) {
        Assert.hasText(locationBarcode, "로케이션 바코드는 필수입니다.");
        Assert.notNull(storageType, "보관 타입은 필수입니다.");
        Assert.notNull(usagePurpose, "보관 목적은 필수입니다.");
        this.locationBarcode = locationBarcode;
        this.storageType = storageType;
        this.usagePurpose = usagePurpose;
    }

    /**
     * 로케이션에 LPN을 등록한다.
     * LPN이 이미 존재하는경우 LocationLPN의 inventory quantity만 증가시킨다.
     * LPN이 존재하지 않으면 LocationLPN을 새로 생성해서 등록한다.
     */
    public void assignLPN(final String lpnBarcode) {
        Assert.hasText(lpnBarcode, "LPN 바코드는 필수입니다.");
    }

    /**
     * 테스트용 메소드입니다.
     */
    public LocationLPN getLocationLPN(final String lpnBarcode) {
        return new LocationLPN();
    }
}