package leejoongseok.wms.inbound.exception;

import leejoongseok.wms.common.NotFoundException;

public class InboundIdNotFoundException extends NotFoundException {
    public InboundIdNotFoundException(final Long inboundId) {
        super(String.format("입고 ID [%d]에 해당하는 입고를 찾을 수 없습니다.", inboundId));
    }
}
