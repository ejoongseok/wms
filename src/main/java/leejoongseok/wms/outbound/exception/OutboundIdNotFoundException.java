package leejoongseok.wms.outbound.exception;

import leejoongseok.wms.common.NotFoundException;

public class OutboundIdNotFoundException extends NotFoundException {
    public OutboundIdNotFoundException(final Long outboundId) {
        super(String.format("출고 ID [%d]에 해당하는 출고를 찾을 수 없습니다.", outboundId));
    }
}
