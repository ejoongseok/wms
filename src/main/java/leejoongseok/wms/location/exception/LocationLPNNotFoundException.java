package leejoongseok.wms.location.exception;

import leejoongseok.wms.common.NotFoundException;

/**
 * 로케이션LPN 목록에서 LPN을 찾을 수 없을 때 발생하는 예외.
 */
public class LocationLPNNotFoundException extends NotFoundException {
    public LocationLPNNotFoundException(final String message) {
        super(message);
    }
}
