package leejoongseok.wms.outbound.domain;

import lombok.RequiredArgsConstructor;

/**
 * 집품 상태
 * 1. 집품대기(READY)
 * 2. 집품중(PROCESSING)
 * 3. 집품완료(COMPLETED)
 */
@RequiredArgsConstructor
public enum PickingStatus {
    READY("집품대기"),
    IN_PROGRESS("집품중"),
    COMPLETED("집품완료");

    private final String description;
}
