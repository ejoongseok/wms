package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.port.WaybillRequester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IssueWaybillTest {


    private IssueWaybill issueWaybill;
    private OutboundRepository outboundRepository;
    private WaybillRequester waybillRequester;

    @BeforeEach
    void setUp() {
        outboundRepository = null;
        waybillRequester = null;
        issueWaybill = new IssueWaybill(outboundRepository, waybillRequester);
    }

    @Test
    @DisplayName("출고에대한 운송장을 발행한다.")
    void issueWaybill() {
        final Long outboundId = 1L;
        issueWaybill.request(outboundId);
    }
}