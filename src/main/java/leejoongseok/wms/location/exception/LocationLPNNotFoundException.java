package leejoongseok.wms.location.exception;

import leejoongseok.wms.common.NotFoundException;

public class LocationLPNNotFoundException extends NotFoundException {
    public LocationLPNNotFoundException(final String message) {
        super(message);
    }
}
