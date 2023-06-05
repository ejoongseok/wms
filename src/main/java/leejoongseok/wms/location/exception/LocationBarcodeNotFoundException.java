package leejoongseok.wms.location.exception;

import leejoongseok.wms.common.NotFoundException;

public class LocationBarcodeNotFoundException extends NotFoundException {
    public LocationBarcodeNotFoundException(final String locationBarcode) {
        super(String.format("로케이션 바코드 [%s]에 해당하는 로케이션을 찾을 수 없습니다.", locationBarcode));
    }
}
