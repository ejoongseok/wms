package leejoongseok.wms.inbound.domain;

import leejoongseok.wms.item.domain.Item;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InboundTest {

    //item
    private final long itemId = 1L;
    //inbound
    private final LocalDateTime orderRequestAt = LocalDateTime.now().minusDays(1);
    private final LocalDateTime estimatedArrivalAt = LocalDateTime.now().plusDays(1);
    private final BigDecimal totalAmount = BigDecimal.valueOf(2000);
    //inboundItem
    private final int receivedQuantity = 2;
    private final BigDecimal unitPurchasePrice = BigDecimal.valueOf(1000);

    @Test
    @DisplayName("입고에 입고 상품을 등록한다.")
    void addInboundItems() {
        final Item item = createItem(itemId);
        final Inbound inbound = new Inbound(orderRequestAt, estimatedArrivalAt, totalAmount);
        final InboundItem inboundItem = createInboundItem(item, receivedQuantity, unitPurchasePrice);

        inbound.addInboundItems(List.of(inboundItem));
    }

    @Test
    @DisplayName("[실패]입고에 입고 상품을 등록한다. - 입고 총액과 입고 상품 개별 총액이 일치하지 않는 경우")
    void fail_wrong_total_amount_addInboundItems() {
        final BigDecimal wrongTotalAmount = BigDecimal.valueOf(3000);
        final Inbound inbound = new Inbound(orderRequestAt, estimatedArrivalAt, wrongTotalAmount);
        final Item item = createItem(itemId);
        final InboundItem wrongInboundItem = createInboundItem(item, receivedQuantity, unitPurchasePrice);

        assertThatThrownBy(() -> {
            inbound.addInboundItems(List.of(wrongInboundItem));
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("입고 상품의 총 금액이 주문 금액과 일치하지 않습니다. 입고총액: 3000, 단품 합산액: 2000");
    }

    @Test
    @DisplayName("[실패]입고에 입고 상품을 등록한다. - 입고상품이 비어있는 경우")
    void fail_empty_inboundItems_addInboundItems() {
        final Inbound inbound = new Inbound(orderRequestAt, estimatedArrivalAt, totalAmount);
        final List<InboundItem> emptyList = List.of();

        assertThatThrownBy(() -> {
            inbound.addInboundItems(emptyList);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("입고 상품은 1개 이상이어야 합니다.");
    }

    private Item createItem(final long itemId) {
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getId), () -> itemId)
                .create();
    }

    private InboundItem createInboundItem(
            final Item item,
            final int receivedQuantity,
            final BigDecimal unitPurchasePrice) {
        return Instancio.of(InboundItem.class)
                .supply(Select.field(InboundItem::getItem), () -> item)
                .supply(Select.field(InboundItem::getReceivedQuantity), () -> receivedQuantity)
                .supply(Select.field(InboundItem::getUnitPurchasePrice), () -> unitPurchasePrice)
                .create();
    }
}