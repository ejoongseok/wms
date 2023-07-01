package leejoongseok.wms.location.domain;

import lombok.RequiredArgsConstructor;

/**
 * 로케이션의 보관 유형을 나타내는 클래스.
 * 보관 유형으로는 cell, pallet, tote 등이 있다.
 * ex) cell: 상품을 진열하기 위한 최소 단위이며 용도는 진열이다.
 * ex) pallet는 보통 장기간/대용량 보관을하는 용도로 사용되고 tote는 이동을 위한 용도로 사용된다.
 */
@RequiredArgsConstructor
public enum StorageType {
    CELL("상품을 진열하기 위한 최소 단위", 1),
    TOTE("상품을 집품 하기위한 용도", 2),
    ;

    private final String description;
    private final Integer size;

    public boolean isCompatibleWith(final StorageType storageType) {
        return size >= storageType.size;
    }
}
