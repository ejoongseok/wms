package leejoongseok.wms.user.exception;

import leejoongseok.wms.common.BadRequestException;

public class UserNameAlreadyExistsException extends BadRequestException {

    public UserNameAlreadyExistsException(final String userName) {
        super(String.format("이미 등록된 사용자 이름입니다. [%s]", userName));
    }
}
