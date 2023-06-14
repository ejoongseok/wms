package leejoongseok.wms.outbound.domain;

import lombok.RequiredArgsConstructor;

/**
 * 출고 상태
 */
@RequiredArgsConstructor
public enum OutboundStatus {
    READY("출고 대기"),
    PICKING("피킹 중"),
    PICKED("피킹 완료"),
    COMPLETE("출고 완료"),
    STOPPED("출고 중지");
    private final String description;
}
