package leejoongseok.wms.inbound.exception;

import leejoongseok.wms.common.BadRequestException;

public class UnconfirmedInboundException extends BadRequestException {
    public UnconfirmedInboundException(final String message) {
        super(message);
    }
}
