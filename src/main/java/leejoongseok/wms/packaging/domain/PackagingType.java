package leejoongseok.wms.packaging.domain;

import lombok.RequiredArgsConstructor;

/**
 * 포장재의 종류
 */
@RequiredArgsConstructor
public enum PackagingType {
    BOX("박스"),
    BAG("봉투"),
    BUBBLE_WRAP("버블랩"),
    PAPER("종이"),
    OTHER("기타");
    private final String description;
}
