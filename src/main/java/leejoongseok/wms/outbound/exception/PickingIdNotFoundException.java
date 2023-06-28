package leejoongseok.wms.outbound.exception;

import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.common.NotFoundException;

public class PickingIdNotFoundException extends NotFoundException {
    public PickingIdNotFoundException(Long pickingId) {
        super(String.format("피킹 ID[%d]에 해당하는 피킹이 없습니다.", pickingId));
    }
}
