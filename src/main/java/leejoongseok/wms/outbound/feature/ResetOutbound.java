package leejoongseok.wms.outbound.feature;

/**
 * 출고를 초기화하면 재고가 변경되거나 하지는 않는다.
 * 출고를 초기화하면 출고에 할당된 토트는 해제된다.
 * 출고를 초기화하면 출고와 연관된 picking도 삭제 된다.
 */
public class ResetOutbound {
    public void request(final Long outboundId) {

    }
}
