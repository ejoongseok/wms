package leejoongseok.wms.inbound.exception;

import leejoongseok.wms.common.BadRequestException;

/**
 * 입고가 확정되어야만 처리할 수 있는 요청에 대해 입고가 확정되지 않았을 경우 발생시킬 예외 클래스
 */
public class UnconfirmedInboundException extends BadRequestException {
    public UnconfirmedInboundException(final String message) {
        super(message);
    }
}
