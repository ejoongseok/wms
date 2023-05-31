package leejoongseok.wms.inbound.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 입고 진행 상태
 * 발주 요청, 입고 확정, 입고 거부
 */
@RequiredArgsConstructor
public enum InboundStatus {
    ORDER_REQUESTED("발주 요청"),
    CONFIRM_INSPECTED("입고 확정"),
    REJECTED("입고 거부");

    @Getter
    private final String description;
}
