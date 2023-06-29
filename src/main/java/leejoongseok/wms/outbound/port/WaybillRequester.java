package leejoongseok.wms.outbound.port;

import leejoongseok.wms.outbound.domain.Outbound;

public class WaybillRequester {
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
