package leejoongseok.wms.packaging.domain;

import lombok.RequiredArgsConstructor;

/**
 * 포장재의 종류
 */
@RequiredArgsConstructor
public enum PackagingType {
    BOX("박스");
    private final String description;
}
