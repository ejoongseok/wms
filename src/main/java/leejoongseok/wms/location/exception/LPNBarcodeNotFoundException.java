package leejoongseok.wms.location.exception;

import leejoongseok.wms.common.BadRequestException;

/**
 * LPN 바코드로 LPN을 찾을 수 없을 때 발생하는 예외.
 */
public class LPNBarcodeNotFoundException extends BadRequestException {
    public LPNBarcodeNotFoundException(final String lpnBarcode) {
        super(String.format("LPN 바코드 [%s]에 해당하는 LPN을 찾을 수 없습니다.", lpnBarcode));
    }
}
