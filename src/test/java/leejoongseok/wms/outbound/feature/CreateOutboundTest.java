package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CreateOutboundTest extends ApiTest {

    @Autowired
    private OutboundRepository outboundRepository;

    @Test
    @DisplayName("출고를 생성한다.")
    void createOutbound() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request()
                .assignLPNToLocation().request()
                .createPackagingMaterial().request()
                .createOutbound().request();

        assertThat(outboundRepository.findById(1L)).isPresent();
    }
}
