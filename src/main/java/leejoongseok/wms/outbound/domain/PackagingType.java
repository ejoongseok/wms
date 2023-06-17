package leejoongseok.wms.outbound.domain;

import lombok.RequiredArgsConstructor;

/**
 * 포장재의 종류
 */
@RequiredArgsConstructor
public enum PackagingType {
    BOX("박스"),
    BAG("봉투"),
    OTHER("기타");
    private final String description;
}
