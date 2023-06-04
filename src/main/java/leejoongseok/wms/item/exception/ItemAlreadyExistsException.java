package leejoongseok.wms.item.exception;

import leejoongseok.wms.common.BadRequestException;

public class ItemAlreadyExistsException extends BadRequestException {
    public ItemAlreadyExistsException(final String itemBarcode) {
        super(String.format("이미 등록된 아이템 바코드입니다. [%s]", itemBarcode));
    }
}
