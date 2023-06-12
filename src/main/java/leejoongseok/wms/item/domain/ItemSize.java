package leejoongseok.wms.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSize {
    @Column(name = "width_millimeter", nullable = false)
    @Comment("상품의 가로 길이 (mm)")
    private Integer widthMillimeter;
    @Column(name = "length_millimeter", nullable = false)
    @Comment("상품의 세로 길이 (mm)")
    private Integer lengthMillimeter;
    @Column(name = "height_millimeter", nullable = false)
    @Comment("상품의 높이 (mm)")
    private Integer heightMillimeter;

    public ItemSize(
            final Integer widthMillimeter,
            final Integer lengthMillimeter,
            final Integer heightMillimeter) {
        Assert.isTrue(null != widthMillimeter && 0 < widthMillimeter,
                "상품의 가로 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(null != lengthMillimeter && 0 < lengthMillimeter,
                "상품의 세로 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(null != heightMillimeter && 0 < heightMillimeter,
                "상품의 높이는 1mm 이상이어야 합니다.");
        this.widthMillimeter = widthMillimeter;
        this.lengthMillimeter = lengthMillimeter;
        this.heightMillimeter = heightMillimeter;
    }

    public Long calculateVolume() {
        return (long) ((long) widthMillimeter * lengthMillimeter * heightMillimeter);
    }
}
