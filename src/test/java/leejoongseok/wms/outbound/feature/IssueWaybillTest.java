package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IssueWaybillTest {


    private IssueWaybill issueWaybill;

    @BeforeEach
    void setUp() {
        issueWaybill = new IssueWaybill();
    }

    @Test
    @DisplayName("출고에대한 운송장을 발행한다.")
    void issueWaybill() {
        final Long outboundId = 1L;
        issueWaybill.request(outboundId);
    }
}