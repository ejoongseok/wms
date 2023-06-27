package leejoongseok.wms.outbound.exception;

import leejoongseok.wms.common.NotFoundException;

public class LPNItemIdNotFoundException extends NotFoundException {
    public LPNItemIdNotFoundException(final String message) {
        super(message);
    }
}
