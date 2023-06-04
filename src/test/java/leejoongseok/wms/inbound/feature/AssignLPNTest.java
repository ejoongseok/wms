package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.ApiTest;
import leejoongseok.wms.Scenario;
import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundItem;
import leejoongseok.wms.inbound.domain.InboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AssignLPNTest extends ApiTest {

    @Autowired
    private InboundRepository inboundRepository;

    @Test
    @DisplayName("입고 아이템의 LPN을 등록한다.")
    void assignLPN() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .assignLPN().request()
        ;

        final Inbound inbound = inboundRepository.testingFindInboundItemFetJoinByInboundId(1L).get();

        final InboundItem inboundItem = inbound.testingGetInboundItemBy(1L);

        assertThat(inboundItem.getLpnBarcode()).isEqualTo("lpnBarcode");
        assertThat(inboundItem.getExpirationAt()).isNotNull();
    }
}
