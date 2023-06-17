package leejoongseok.wms.item.exception;

import leejoongseok.wms.common.BadRequestException;

/**
 * 이미 등록된 상품 바코드 예외
 */
public class ItemBarcodeAlreadyExistsException extends BadRequestException {
    public ItemBarcodeAlreadyExistsException(final String itemBarcode) {
        super(String.format("이미 등록된 아이템 바코드입니다. [%s]", itemBarcode));
    }
}
