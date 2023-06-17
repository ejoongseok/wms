package leejoongseok.wms.inbound.exception;

import leejoongseok.wms.common.BadRequestException;

/**
 * LPN 바코드가 이미 존재할 경우 발생시킬 예외 클래스
 */
public class LPNBarcodeAlreadyExistsException extends BadRequestException {
    public LPNBarcodeAlreadyExistsException(final String lpnBarcode) {
        super(String.format("LPN 바코드가 이미 존재합니다. lpnBarcode: %s", lpnBarcode));
    }
}
