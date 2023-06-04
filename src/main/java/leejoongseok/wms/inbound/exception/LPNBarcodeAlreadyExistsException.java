package leejoongseok.wms.inbound.exception;

import leejoongseok.wms.common.BadRequestException;

public class LPNBarcodeAlreadyExistsException extends BadRequestException {
    public LPNBarcodeAlreadyExistsException(final String lpnBarcode) {
        super(String.format("LPN 바코드가 이미 존재합니다. lpnBarcode: %s", lpnBarcode));
    }
}
