package leejoongseok.wms.outbound.domain;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboundItemTest {

    @Test
    @DisplayName("출고 상품을 분할 한다.")
    void split() {
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);

        final int quantityOfSplit = 1;
        final OutboundItem splittedOutboundItem = outboundItem.split(quantityOfSplit);
        assertThat(outboundItem.getOutboundQuantity()).isZero();
        assertThat(splittedOutboundItem.getOutboundQuantity()).isEqualTo(1);
        assertThat(outboundItem.getItem()).isEqualTo(splittedOutboundItem.getItem());
        assertThat(outboundItem.getUnitPrice()).isEqualTo(splittedOutboundItem.getUnitPrice());
    }

    private OutboundItem createOutboundItem(final Integer outboundQuantity) {
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundQuantity)
                .create();
    }

    @Test
    @DisplayName("출고 상품을 분할 한다.[ 실패 : 분할 수량이 출고 수량보다 많을 경우 ]")
    void fail_split_invalid_quantity_of_split() {
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);

        final int overQuantityOfSplit = 2;
        assertThatThrownBy(() -> {
            outboundItem.split(overQuantityOfSplit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("분할 수량은 출고 수량보다 작거나 같아야 합니다.출고 수량: 1, 분할 수량: 2");
    }

    @Test
    @DisplayName("출고 상품을 분할 한다.[ 실패 : 분할 수량 0 이하 ]")
    void fail_split_zero_quantity_of_split() {
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundItem(outboundQuantity);

        final int zeroQuantityOfSplit = 0;
        assertThatThrownBy(() -> {
            outboundItem.split(zeroQuantityOfSplit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출고 수량은 0보다 커야합니다.");
    }
}