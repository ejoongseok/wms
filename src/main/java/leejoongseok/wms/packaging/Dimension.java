package leejoongseok.wms.packaging;

import org.springframework.util.Assert;

public class Dimension {
    private final Integer innerWidthMillimeter;
    private final Integer innerHeightMillimeter;
    private final Integer innerLengthMillimeter;
    private final Integer outerWidthMillimeter;
    private final Integer outerHeightMillimeter;
    private final Integer outerLengthMillimeter;

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
