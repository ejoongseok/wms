package leejoongseok.wms.outbound.domain;

import lombok.RequiredArgsConstructor;

/**
 * 포장 완충재
 */
@RequiredArgsConstructor
public enum CushioningMaterial {
    NONE(0, "완충재 없음"),
    BUBBLE_WRAP(1000, "뽁뽁이"),
    AIR_PILLOW(3000, "에어 쿠션"),
    PAPER_PADDING(2000, "종이 완충재");

    private final int volume;
    private final String description;
}
