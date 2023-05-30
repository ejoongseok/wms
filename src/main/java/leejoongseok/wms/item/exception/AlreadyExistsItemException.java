package leejoongseok.wms.item.exception;

public class AlreadyExistsItemException extends RuntimeException {
    public AlreadyExistsItemException(final String itemBarcode) {
        super(String.format("이미 등록된 아이템 바코드입니다. [%s]", itemBarcode));
    }
}
