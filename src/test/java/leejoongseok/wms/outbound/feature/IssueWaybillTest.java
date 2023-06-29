package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class IssueWaybillTest extends ApiTest {

    @Autowired
    private OutboundRepository outboundRepository;


    @Test
    @DisplayName("출고에대한 운송장을 발행한다.")
    void issueWaybill() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request(2)
                .createPackagingMaterial().request()
                .createOutbound().request()
                .issueWaybill().request();

        assertThat(outboundRepository.findById(1L).get().hasTrackingNumber()).isTrue();
    }
}