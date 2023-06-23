package leejoongseok.wms.outbound.exception;

import leejoongseok.wms.common.NotFoundException;

/**
 * 상품 ID를 찾을 수 없을 때 발생하는 예외
 */
public class ItemIdNotFoundException extends NotFoundException {
    public ItemIdNotFoundException(final Long itemId) {
        super("상품을 찾을 수 없습니다. itemId=" + itemId);
    }
}
