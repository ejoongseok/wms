package leejoongseok.wms.outbound.exception;

import leejoongseok.wms.common.NotFoundException;

public class ItemIdNotFoundException extends NotFoundException {
    public ItemIdNotFoundException(final Long itemId) {
        super("상품을 찾을 수 없습니다. itemId=" + itemId);
    }
}
