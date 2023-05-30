package leejoongseok.wms.item.domain;

import lombok.RequiredArgsConstructor;

/**
 * 상품의 카테고리
 */
@RequiredArgsConstructor
public enum Category {
    ELECTRONICS("전자제품");

    private final String description;

}
