package leejoongseok.wms.outbound.exception;

import leejoongseok.wms.common.NotFoundException;

public class LocationLPNIdNotFoundException extends NotFoundException {
    public LocationLPNIdNotFoundException(final Long locationLPNId) {
        super("해당하는 Location LPN ID를 찾을 수 없습니다. locationLPNId=" + locationLPNId);
    }
}
