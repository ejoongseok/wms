package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.inbound.feature.CreateInbound;
import leejoongseok.wms.item.domain.ItemRepository;
import leejoongseok.wms.outbound.domain.Order;
import leejoongseok.wms.outbound.domain.OrderItem;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.port.LoadOrderPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SplitToOutboundTest extends ApiTest {

    @Autowired
    private SplitToOutbound splitToOutbound;

    @MockBean
    private LoadOrderPort loadOrderPort;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OutboundRepository outboundRepository;

    @Test
    @DisplayName("대기중인 출고건을 분할한다.")
    void splitToOutbound() {
        new Scenario()
                .createItem().request()
                .createItem().itemBarcode("itemBarcode-").request();
        final Order order = createOrder();
        Mockito.when(loadOrderPort.getBy(1L)).thenReturn(order);
        new Scenario()
                .createInbound()
                .itemRequests(List.of(
                        new CreateInbound.Request.ItemRequest(
                                1L,
                                2,
                                BigDecimal.valueOf(500),
                                "description"),
                        new CreateInbound.Request.ItemRequest(
                                2L,
                                2,
                                BigDecimal.valueOf(500),
                                "description")
                ))
                .totalAmount(BigDecimal.valueOf(2000))
                .request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLPN()
                .inboundItemId(2L)
                .lpnBarcode("lpnBarcode-")
                .request()
                .createLocation().request()
                .assignLPNToLocation().request()
                .assignLPNToLocation().request()
                .assignLPNToLocation().lpnBarcode("lpnBarcode-").request()
                .assignLPNToLocation().lpnBarcode("lpnBarcode-").request()
                .createPackagingMaterial()
                .maxWeightGram(1000)
                .innerHeightMillimeter(100)
                .innerLengthMillimeter(100)
                .innerWidthMillimeter(100)
                .request()
                .createOutbound().request();
        assertThat(outboundRepository.testingFindById(1L).get().getOutboundItems()).hasSize(2);

        new Scenario()
                .splitToOutbound().request();

        final Outbound outbound = outboundRepository.testingFindById(1L).get();
        final Outbound splittedOutbound = outboundRepository.testingFindById(2L).get();
        final List<OutboundItem> outboundItems = outbound.getOutboundItems();
        assertThat(outboundItems.get(0).getOutboundQuantity()).isEqualTo(2);
        assertThat(outboundItems.get(0).getId()).isEqualTo(2L);
        assertThat(outboundItems).hasSize(1);
        final List<OutboundItem> splittedOutboundOutboundItems = splittedOutbound.getOutboundItems();
        assertThat(splittedOutboundOutboundItems.get(0).getOutboundQuantity()).isEqualTo(2);
        assertThat(splittedOutboundOutboundItems.get(0).getId()).isEqualTo(3L);
    }

    private Order createOrder() {
        final Order order = new Order(
                1L,
                "서울시 강남구 테헤란로 427",
                "홍길동",
                "ejoongseok@gmail.com",
                "010-1234-5678",
                "12345",
                LocalDate.now().plusDays(1),
                false,
                "출고 요청 사항",
                "배송 요청 사항",
                LocalDateTime.now(),
                List.of(
                        new OrderItem(
                                itemRepository.findById(1L).get(),
                                2,
                                BigDecimal.valueOf(1000)
                        ),
                        new OrderItem(
                                itemRepository.findById(2L).get(),
                                2,
                                BigDecimal.valueOf(1000)
                        ))

        );
        return order;
    }
}
