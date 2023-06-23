package leejoongseok.wms.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

/**
 * 상품의 크기
 * 가로 세로 높이 (mm)
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemSize {
    @Column(name = "width_in_millimeters", nullable = false)
    @Comment("상품의 가로 길이 (mm)")
    private Integer widthInMillimeters;
    @Column(name = "length_in_millimeters", nullable = false)
    @Comment("상품의 세로 길이 (mm)")
    private Integer lengthInMillimeters;
    @Column(name = "height_in_millimeters", nullable = false)
    @Comment("상품의 높이 (mm)")
    private Integer heightInMillimeters;

    public ItemSize(
            final Integer widthInMillimeters,
            final Integer lengthInMillimeters,
            final Integer heightInMillimeters) {
        validateConstructor(
                widthInMillimeters,
                lengthInMillimeters,
                heightInMillimeters);
        this.widthInMillimeters = widthInMillimeters;
        this.lengthInMillimeters = lengthInMillimeters;
        this.heightInMillimeters = heightInMillimeters;
    }

    private void validateConstructor(
            final Integer widthInMillimeter,
            final Integer lengthInMillimeter,
            final Integer heightInMillimeter) {
        Assert.isTrue(null != widthInMillimeter && 0 < widthInMillimeter,
                "상품의 가로 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(null != lengthInMillimeter && 0 < lengthInMillimeter,
                "상품의 세로 길이는 1mm 이상이어야 합니다.");
        Assert.isTrue(null != heightInMillimeter && 0 < heightInMillimeter,
                "상품의 높이는 1mm 이상이어야 합니다.");
    }

    public Long calculateVolume() {
        return (long) widthInMillimeters * lengthInMillimeters * heightInMillimeters;
    }
}
