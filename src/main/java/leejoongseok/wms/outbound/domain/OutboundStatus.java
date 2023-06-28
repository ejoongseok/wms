package leejoongseok.wms.outbound.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 출고 상태
 */
@Getter
@RequiredArgsConstructor
public enum OutboundStatus {
    READY("출고 대기"),
    PICKING_READY("피킹 대기(토트만 할당된 상태)"),
    PICKING_IN_PROGRESS("피킹 중"),
    PICKED("피킹 완료"),
    COMPLETE("출고 완료"),
    STOPPED("출고 중지");
    private final String description;
}
