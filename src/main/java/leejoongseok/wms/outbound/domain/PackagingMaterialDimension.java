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
    @Column(name = "inner_width_millimeter", nullable = false)
    @Comment("내부 폭 (mm)")
    @Getter
    private Integer innerWidthMillimeter;
    @Column(name = "inner_height_millimeter", nullable = false)
    @Comment("내부 높이 (mm)")
    @Getter
    private Integer innerHeightMillimeter;
    @Column(name = "inner_length_millimeter", nullable = false)
    @Comment("내부 길이 (mm)")
    @Getter
    private Integer innerLengthMillimeter;
    @Column(name = "outer_width_millimeter", nullable = false)
    @Comment("외부 폭 (mm)")
    private Integer outerWidthMillimeter;
    @Column(name = "outer_height_millimeter", nullable = false)
    @Comment("외부 높이 (mm)")
    private Integer outerHeightMillimeter;
    @Column(name = "outer_length_millimeter", nullable = false)
    @Comment("외부 길이 (mm)")
    private Integer outerLengthMillimeter;
    @Column(name = "thickness_in_millimeter", nullable = false)
    @Comment("두께 (mm)")
    @Getter
    private Integer thicknessInMillimeter;

    public PackagingMaterialDimension(
            final Integer innerWidthMillimeter,
            final Integer innerHeightMillimeter,
            final Integer innerLengthMillimeter,
            final Integer outerWidthMillimeter,
            final Integer outerHeightMillimeter,
            final Integer outerLengthMillimeter,
            final Integer thicknessInMillimeter) {
        Assert.isTrue(1 <= innerWidthMillimeter, "내부 폭은 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= innerHeightMillimeter, "내부 높이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= innerLengthMillimeter, "내부 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerWidthMillimeter, "외부 폭은 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerHeightMillimeter, "외부 높이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerLengthMillimeter, "외부 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= thicknessInMillimeter, "두께는 1mm 이상이어야 합니다.");
        this.innerWidthMillimeter = innerWidthMillimeter;
        this.innerHeightMillimeter = innerHeightMillimeter;
        this.innerLengthMillimeter = innerLengthMillimeter;
        this.outerWidthMillimeter = outerWidthMillimeter;
        this.outerHeightMillimeter = outerHeightMillimeter;
        this.outerLengthMillimeter = outerLengthMillimeter;
        this.thicknessInMillimeter = thicknessInMillimeter;
    }

    public Long calculatePackageableVolume() {
        final long packageableVolume = (long) innerWidthMillimeter * innerHeightMillimeter * innerLengthMillimeter;
        return packageableVolume - calculateThicknessVolume();
    }

    /**
     * 두께의 부피를 계산합니다.
     * 두께의 부피는 부피의 세제곱(가로x세로x높이) 만큼을 차지한다.
     */
    private int calculateThicknessVolume() {
        return thicknessInMillimeter * thicknessInMillimeter * thicknessInMillimeter;
    }
}
