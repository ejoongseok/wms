package leejoongseok.wms.location.domain;

import lombok.RequiredArgsConstructor;

/**
 * 로케이션의 용도를 나타내는 클래스.
 * 용도로는 stow, move, rejectZone 등이 있다.
 * ex) stow: 상품을 진열하기 위한 용도
 * ex) move: 상품을 이동하기 위한 용도
 * ex) rejectZone: 반품 상품을 보관하기 위한 용도
 */
@RequiredArgsConstructor
public enum UsagePurpose {
    STOW("상품을 진열하기 위한 용도"),
    MOVE("상품을 이동하기 위한 용도");

    private final String description;
}
