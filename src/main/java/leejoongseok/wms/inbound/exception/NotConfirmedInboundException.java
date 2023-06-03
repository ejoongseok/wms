package leejoongseok.wms.inbound.exception;

import leejoongseok.wms.common.BadRequestException;

public class NotConfirmedInboundException extends BadRequestException {
    public NotConfirmedInboundException(final String message) {
        super(message);
    }
}
