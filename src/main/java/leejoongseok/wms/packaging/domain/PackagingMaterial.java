package leejoongseok.wms.packaging.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

@Entity
@Table(name = "packaging_material")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("포장재")
@ToString
public class PackagingMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("포장재 ID")
    @Getter
    private Long id;
    @Embedded
    @Getter
    private PackagingMaterialDimension packagingMaterialDimension;
    @Column(name = "weight_in_grams", nullable = false)
    @Comment("무게(g)")
    private Integer weightInGrams;
    @Column(name = "packaging_type", nullable = false)
    @Comment("포장재 종류")
    @Enumerated(EnumType.STRING)
    private PackagingType packagingType;
    @Column(name = "thickness_in_millimeter", nullable = false)
    @Comment("두께 (mm)")
    @Getter
    private Integer thicknessInMillimeter;
    @Column(name = "name", nullable = false)
    @Comment("포장재 이름")
    @Getter
    private String name;
    @Column(name = "code", nullable = true)
    @Comment("포장재 코드")
    private String code;
    @Column(name = "max_weight_in_grams", nullable = false)
    @Comment("최대 무게(g)")
    @Getter
    private Integer maxWeightInGrams;
    @Column(name = "description", nullable = true)
    @Comment("설명")
    private String description;

    public PackagingMaterial(
            final PackagingMaterialDimension packagingMaterialDimension,
            final Integer weightInGrams,
            final PackagingType packagingType,
            final Integer thicknessInMillimeter,
            final String name,
            final String code,
            final Integer maxWeightInGrams,
            final String description) {
        Assert.notNull(packagingMaterialDimension, "포장재 치수는 필수입니다.");
        Assert.isTrue(1 <= weightInGrams, "무게는 1g 이상이어야 합니다.");
        Assert.isTrue(1 <= thicknessInMillimeter, "두께는 1mm 이상이어야 합니다.");
        Assert.hasText(name, "포장재 이름은 필수입니다.");
        Assert.isTrue(1 <= maxWeightInGrams, "최대 무게는 1g 이상이어야 합니다.");
        Assert.notNull(packagingType, "포장재 종류는 필수입니다.");
        this.packagingMaterialDimension = packagingMaterialDimension;
        this.weightInGrams = weightInGrams;
        this.packagingType = packagingType;
        this.thicknessInMillimeter = thicknessInMillimeter;
        this.name = name;
        this.code = code;
        this.maxWeightInGrams = maxWeightInGrams;
        this.description = description;
    }

    public boolean isPackageable(
            final Long totalVolume,
            final Long totalWeightInGrams) {
        final boolean isPackageableVolume = calculatePackageableVolume() >= totalVolume;
        final boolean isPackageableWeightInGrams = maxWeightInGrams >= totalWeightInGrams;
        return isPackageableVolume && isPackageableWeightInGrams;
    }

    public Long calculatePackageableVolume() {
        return packagingMaterialDimension.calculatePackageableVolume() - calculateThicknessVolume();
    }

    /**
     * 두께의 부피를 계산합니다.
     * 두께의 부피는 부피의 세제곱(가로x세로x높이) 만큼을 차지한다.
     */
    private int calculateThicknessVolume() {
        return thicknessInMillimeter * thicknessInMillimeter * thicknessInMillimeter;
    }
}
