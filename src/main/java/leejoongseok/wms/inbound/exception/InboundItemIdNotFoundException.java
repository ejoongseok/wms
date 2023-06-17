package leejoongseok.wms.inbound.exception;

import leejoongseok.wms.common.NotFoundException;

/**
 * 입고 아이템 ID로 조회했을 때 해당 데이터가 존재하지 않을 경우 발생시킬 예외 클래스
 */
public class InboundItemIdNotFoundException extends NotFoundException {
    public InboundItemIdNotFoundException(final Long inboundItemId) {
        super(String.format("입고 아이템 ID [%d]에 해당하는 입고 아이템을 찾을 수 없습니다.", inboundItemId));
    }
}
