package leejoongseok.wms.item.domain;

import lombok.RequiredArgsConstructor;

/**
 * 상품의 보관 온도대역
 */
@RequiredArgsConstructor
public enum TemperatureZone {
    FROZEN("냉동"),
    REFRIGERATED("냉장"),
    ROOM_TEMPERATURE("상온");

    private final String description;
}
