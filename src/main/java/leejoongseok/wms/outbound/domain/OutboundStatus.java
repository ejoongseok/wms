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
    PICKING_COMPLETED("피킹 완료"),
    INSPECTION_PASSED("검수 통과"),
    PACKING_IN_PROGRESS("포장 중"),
    PACKING_COMPLETED("포장 완료"),
    STOPPED("출고 중지"),
    COMPLETE("출고 완료");
    private final String description;
}
