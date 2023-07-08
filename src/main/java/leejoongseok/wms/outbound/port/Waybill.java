package leejoongseok.wms.outbound.port;

import leejoongseok.wms.outbound.domain.Outbound;
import org.springframework.stereotype.Component;

/**
 * 출고의 정보로 운송장을 발행하는 기능을 담당
 */
@Component
public class Waybill {
    public String request(final Outbound outbound) {
        validateWaybillRequest(outbound);
        return "trackingNumber";
    }

    private void validateWaybillRequest(final Outbound outbound) {
        if (outbound.hasTrackingNumber()) {
            throw new IllegalStateException("이미 운송장이 발행되었습니다.");
        }
    }
}
