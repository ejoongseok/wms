package leejoongseok.wms.location.exception;

import leejoongseok.wms.common.BadRequestException;

public class LocationBarcodeAlreadyExistsException extends BadRequestException {
    public LocationBarcodeAlreadyExistsException(final String locationBarcode) {
        super(String.format("로케이션 바코드가 이미 존재합니다. locationBarcode: %s", locationBarcode));
    }
}
