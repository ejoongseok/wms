package leejoongseok.wms.inbound.exception;

public class AlreadyExistsLPNException extends RuntimeException {
    public AlreadyExistsLPNException(final Long inboundItemId) {
        super(String.format("입고상품에 이미 등록된 LPN이 존재합니다. 입고상품 ID:[%s]", inboundItemId));
    }
}
