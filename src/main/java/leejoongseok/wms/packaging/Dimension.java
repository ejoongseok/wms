package leejoongseok.wms.packaging;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dimension {
    @Column(name = "inner_width_millimeter", nullable = false)
    @Comment("내부 폭 (mm)")
    private Integer innerWidthMillimeter;
    @Column(name = "inner_height_millimeter", nullable = false)
    @Comment("내부 높이 (mm)")
    private Integer innerHeightMillimeter;
    @Column(name = "inner_length_millimeter", nullable = false)
    @Comment("내부 길이 (mm)")
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

    public Dimension(
            final Integer innerWidthMillimeter,
            final Integer innerHeightMillimeter,
            final Integer innerLengthMillimeter,
            final Integer outerWidthMillimeter,
            final Integer outerHeightMillimeter,
            final Integer outerLengthMillimeter) {
        Assert.isTrue(1 <= innerWidthMillimeter, "내부 폭은 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= innerHeightMillimeter, "내부 높이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= innerLengthMillimeter, "내부 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerWidthMillimeter, "외부 폭은 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerHeightMillimeter, "외부 높이는 1mm 이상이어야 합니다.");
        Assert.isTrue(1 <= outerLengthMillimeter, "외부 길이는 1mm 이상이어야 합니다.");
        this.innerWidthMillimeter = innerWidthMillimeter;
        this.innerHeightMillimeter = innerHeightMillimeter;
        this.innerLengthMillimeter = innerLengthMillimeter;
        this.outerWidthMillimeter = outerWidthMillimeter;
        this.outerHeightMillimeter = outerHeightMillimeter;
        this.outerLengthMillimeter = outerLengthMillimeter;

    }
}
