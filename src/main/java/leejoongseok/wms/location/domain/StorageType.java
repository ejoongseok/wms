package leejoongseok.wms.location.domain;

import lombok.RequiredArgsConstructor;

/**
 * 보관 유형
 */
@RequiredArgsConstructor
public enum StorageType {
    CELL("상품을 진열하기 위한 최소 단위");

    private final String description;
}
