package leejoongseok.wms.location.exception;

import leejoongseok.wms.common.NotFoundException;

/**
 * 로케이션 바코드로 로케이션을 찾을 수 없을 때 발생하는 예외.
 */
public class LocationBarcodeNotFoundException extends NotFoundException {
    public LocationBarcodeNotFoundException(final String locationBarcode) {
        super(String.format("로케이션 바코드 [%s]에 해당하는 로케이션을 찾을 수 없습니다.", locationBarcode));
    }
}
