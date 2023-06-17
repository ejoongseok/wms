package leejoongseok.wms.location.exception;

import leejoongseok.wms.common.BadRequestException;

/**
 * 로케이션 바코드가 이미 존재할 경우 발생하는 예외.
 */
public class LocationBarcodeAlreadyExistsException extends BadRequestException {
    public LocationBarcodeAlreadyExistsException(final String locationBarcode) {
        super(String.format("로케이션 바코드가 이미 존재합니다. locationBarcode: %s", locationBarcode));
    }
}
