package leejoongseok.wms.common;

/**
 * 클라이언트의 잘못된 요청을 처리하기 위한 예외 클래스
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(final String message) {
        super(message);
    }
}
