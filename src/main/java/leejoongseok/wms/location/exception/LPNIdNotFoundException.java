package leejoongseok.wms.location.exception;

import leejoongseok.wms.common.NotFoundException;

public class LPNIdNotFoundException extends NotFoundException {
    public LPNIdNotFoundException(final Long lpnId) {
        super("해당하는 LPN ID를 찾을 수 없습니다. lpnId=" + lpnId);
    }
}
