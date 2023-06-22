package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.item.domain.ItemRepository;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SplitToOutboundTest extends ApiTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OutboundRepository outboundRepository;

    @Test
    @DisplayName("대기중인 출고건을 분할한다. (1개의 출고건을 2개로 분할)")
    void splitToOutbound() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLocation().request()
                .assignLPNToLocation().request(2)
                .createPackagingMaterial()
                .maxWeightGram(1000)
                .innerHeightMillimeter(100)
                .innerLengthMillimeter(100)
                .innerWidthMillimeter(100)
                .request()
                .createOutbound().request();
        assertThat(outboundRepository.testingFindById(1L).get().getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(2);

        new Scenario()
                .splitToOutbound().request();

        final Outbound outbound = outboundRepository.testingFindById(1L).get();
        final Outbound splittedOutbound = outboundRepository.testingFindById(2L).get();
        final List<OutboundItem> outboundItems = outbound.getOutboundItems();
        assertThat(outboundItems.get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(outboundItems.get(0).getId()).isEqualTo(1L);
        assertThat(outboundItems).hasSize(1);
        final List<OutboundItem> splittedOutboundOutboundItems = splittedOutbound.getOutboundItems();
        assertThat(splittedOutboundOutboundItems.get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(splittedOutboundOutboundItems.get(0).getId()).isEqualTo(2L);
    }
}
