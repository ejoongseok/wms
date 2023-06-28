package leejoongseok.wms.outbound.exception;

import leejoongseok.wms.common.NotFoundException;

public class LocationLPNBarcodeNotFoundException extends NotFoundException {
    public LocationLPNBarcodeNotFoundException(
            final String locationBarcode,
            final String lpnBarcode) {
        super(String.format("로케이션 바코드[%s]에 해당하는 상품의 LPN 바코드[%s]가 없습니다.", locationBarcode, lpnBarcode));
    }
}
