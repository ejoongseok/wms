package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CreateInboundTest extends ApiTest {

    @Autowired
    private InboundRepository inboundRepository;

    @Test
    @DisplayName("입고를 등록한다.")
    void createInbound() {
        Scenario.createItem().request()
                .createInbound().request();

        final Inbound inbound = inboundRepository.findById(1L).get();
        assertThat(inbound).isNotNull();
    }
}
