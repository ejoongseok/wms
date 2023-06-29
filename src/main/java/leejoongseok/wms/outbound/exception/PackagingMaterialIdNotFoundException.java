package leejoongseok.wms.outbound.exception;

import leejoongseok.wms.common.NotFoundException;

public class PackagingMaterialIdNotFoundException extends NotFoundException {
    public PackagingMaterialIdNotFoundException(final Long packagingMaterialId) {
        super(String.format("포장자재 ID[%d]에 해당하는 포장자재가 없습니다.", packagingMaterialId));
    }
}
