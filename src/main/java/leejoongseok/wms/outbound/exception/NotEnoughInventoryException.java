package leejoongseok.wms.outbound.exception;

public class NotEnoughInventoryException extends RuntimeException {
    public NotEnoughInventoryException(final String message) {
        super(message);
    }
}
