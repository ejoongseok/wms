package leejoongseok.wms.outbound.exception;

import leejoongseok.wms.common.NotFoundException;

/**
 * 출고 상품 ID를 찾을 수 없을 때 발생하는 예외
 */
public class OutboundItemIdNotFoundException extends NotFoundException {
    public OutboundItemIdNotFoundException(final Long outboundItemId) {
        super(String.format("출고 상품 ID [%d]에 해당하는 출고 상품을 찾을 수 없습니다.", outboundItemId));
    }
}
