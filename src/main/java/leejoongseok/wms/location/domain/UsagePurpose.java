package leejoongseok.wms.location.domain;

import lombok.RequiredArgsConstructor;

/**
 * 로케이션의 용도
 * ex) cell: 상품을 진열하기 위한 최소 단위이며 용도는 진열이다.
 * ex) pallet는 보통 장기간/대용량 보관을하는 용도로 사용되고 tote는 이동을 위한 용도로 사용된다.
 */
@RequiredArgsConstructor
public enum UsagePurpose {
    STOW("상품을 진열하기 위한 용도");

    private final String description;
}
