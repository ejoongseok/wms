package leejoongseok.wms.inbound.exception;

import leejoongseok.wms.common.NotFoundException;

public class InboundItemIdNotFoundException extends NotFoundException {
    public InboundItemIdNotFoundException(final Long inboundItemId) {
        super(String.format("입고 아이템 ID [%d]에 해당하는 입고 아이템을 찾을 수 없습니다.", inboundItemId));
    }
}
