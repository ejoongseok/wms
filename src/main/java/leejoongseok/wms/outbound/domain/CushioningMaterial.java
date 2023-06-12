package leejoongseok.wms.outbound.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 포장 완충재
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
}
