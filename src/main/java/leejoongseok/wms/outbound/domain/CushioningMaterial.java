package leejoongseok.wms.outbound.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

/**
 * 포장에서 사용하는 완충재
 */
@RequiredArgsConstructor
public enum CushioningMaterial {
    NONE(0, 0, "완충재 없음"),
    BUBBLE_WRAP(1000, 10, "뽁뽁이"),
    AIR_PILLOW(3000, 10, "에어 쿠션"),
    PAPER_PADDING(2000, 5, "종이 완충재");

    @Getter
    private final int volume;
    @Getter
    private final int weightInGrams;
    private final String description;

    /**
     * 완충재 수량에 따른 총 무게를 계산합니다.
     */
    public int calculateTotalWeightInGrams(
            final Integer cushioningMaterialQuantity) {
        Assert.notNull(cushioningMaterialQuantity, "완충재 수량은 필수입니다.");
        if (0 > cushioningMaterialQuantity) {
            throw new IllegalArgumentException("완충재 수량은 0보다 커야합니다.");
        }
        return weightInGrams * cushioningMaterialQuantity;
    }

    /**
     * 완충재 수량에 따른 총 부피를 계산합니다.
     */
    public int calculateTotalVolume(
            final Integer cushioningMaterialQuantity) {
        Assert.notNull(cushioningMaterialQuantity, "완충재 수량은 필수입니다.");
        if (0 > cushioningMaterialQuantity) {
            throw new IllegalArgumentException("완충재 수량은 0보다 커야합니다.");
        }
        return volume * cushioningMaterialQuantity;
    }
}
