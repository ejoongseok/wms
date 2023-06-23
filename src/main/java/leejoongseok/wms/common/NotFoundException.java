package leejoongseok.wms.common;

/**
 * 조회했을 때 해당 데이터가 존재하지 않을 경우 발생시킬 예외 클래스
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
    }
}
