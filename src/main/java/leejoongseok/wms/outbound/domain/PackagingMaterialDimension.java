package leejoongseok.wms.outbound.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

/**
 * 포장재의 크기
 * 내부 크기와 외부 크기, 두께를 모두 가지고 있습니다.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PackagingMaterialDimension {
    @Column(name = "inner_width_in_millimeters", nullable = false)
    @Comment("내부 폭 (mm)")
    @Getter
    private Integer innerWidthInMillimeters;
    @Column(name = "inner_height_in_millimeters", nullable = false)
    @Comment("내부 높이 (mm)")
    @Getter
    private Integer innerHeightInMillimeters;
    @Column(name = "inner_length_in_millimeters", nullable = false)
    @Comment("내부 길이 (mm)")
    @Getter
    private Integer innerLengthInMillimeters;
    @Column(name = "outer_width_in_millimeters", nullable = false)
    @Comment("외부 폭 (mm)")
    private Integer outerWidthInMillimeters;
    @Column(name = "outer_height_in_millimeters", nullable = false)
    @Comment("외부 높이 (mm)")
    private Integer outerHeightInMillimeters;
    @Column(name = "outer_length_in_millimeters", nullable = false)
    @Comment("외부 길이 (mm)")
    private Integer outerLengthInMillimeters;
    @Column(name = "thickness_in_millimeters", nullable = false)
    @Comment("두께 (mm)")
    @Getter
    private Integer thicknessInMillimeters;

    public PackagingMaterialDimension(
            final Integer innerWidthInMillimeters,
            final Integer innerHeightInMillimeters,
            final Integer innerLengthInMillimeter,
            final Integer outerWidthInMillimeter,
            final Integer outerHeightInMillimeter,
            final Integer outerLengthInMillimeter,
            final Integer thicknessInMillimeter) {
        Assert.isTrue(1 <= innerWidthInMillimeters,
                "내부 폭은 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= innerHeightInMillimeters,
                "내부 높이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= innerLengthInMillimeter,
                "내부 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerWidthInMillimeter,
                "외부 폭은 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerHeightInMillimeter,
                "외부 높이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerLengthInMillimeter,
                "외부 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= thicknessInMillimeter,
                "두께는 1mm 이상이어야 합니다.");
        this.innerWidthInMillimeters = innerWidthInMillimeters;
        this.innerHeightInMillimeters = innerHeightInMillimeters;
        innerLengthInMillimeters = innerLengthInMillimeter;
        outerWidthInMillimeters = outerWidthInMillimeter;
        outerHeightInMillimeters = outerHeightInMillimeter;
        outerLengthInMillimeters = outerLengthInMillimeter;
        thicknessInMillimeters = thicknessInMillimeter;
    }

    public Long calculatePackageableVolume() {
        final long packageableVolume = (long) innerWidthInMillimeters * innerHeightInMillimeters * innerLengthInMillimeters;
        return packageableVolume - calculateThicknessVolume();
    }

    /**
     * 두께의 부피를 계산합니다.
     * 두께의 부피는 부피의 세제곱(가로x세로x높이) 만큼을 차지한다.
     */
    private int calculateThicknessVolume() {
        return thicknessInMillimeters * thicknessInMillimeters * thicknessInMillimeters;
    }
}
