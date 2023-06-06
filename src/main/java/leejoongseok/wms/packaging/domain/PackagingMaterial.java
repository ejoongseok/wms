package leejoongseok.wms.packaging.domain;

import leejoongseok.wms.packaging.Dimension;
import org.springframework.util.Assert;

public class PackagingMaterial {
    private final Dimension dimension;
    private final Integer weightInGrams;
    private final PackagingType packagingType;
    private final Integer thickness;
    private final String name;
    private final String code;
    private final Integer maxWeightInGrams;
    private final String description;

    public PackagingMaterial(
            final Dimension dimension,
            final Integer weightInGrams,
            final PackagingType packagingType,
            final Integer thickness,
            final String name,
            final String code,
            final Integer maxWeightInGrams,
            final String description) {
        Assert.notNull(dimension, "포장재 크기는 필수입니다.");
        Assert.isTrue(1 <= weightInGrams, "무게는 1g 이상이어야 합니다.");
        Assert.isTrue(1 <= thickness, "두께는 1mm 이상이어야 합니다.");
        Assert.hasText(name, "포장재 이름은 필수입니다.");
        Assert.isTrue(1 <= maxWeightInGrams, "제한 무게는 1g 이상이어야 합니다.");
        Assert.notNull(packagingType, "포장재 종류는 필수입니다.");
        this.dimension = dimension;
        this.weightInGrams = weightInGrams;
        this.packagingType = packagingType;
        this.thickness = thickness;
        this.name = name;
        this.code = code;
        this.maxWeightInGrams = maxWeightInGrams;
        this.description = description;

    }
}
