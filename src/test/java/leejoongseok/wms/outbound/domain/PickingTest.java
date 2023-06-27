package leejoongseok.wms.outbound.domain;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PickingTest {

    @Test
    @DisplayName("집품한 상품이 있는지 확인한다.")
    void hasPickedItem() {
        final Picking picking = Instancio.of(Picking.class)
                .supply(Select.field(Picking::getPickedQuantity), () -> 1)
                .create();

        final boolean hasPickedItem = picking.hasPickedItem();

        assertThat(hasPickedItem).isTrue();
    }

    @Test
    @DisplayName("집품한 상품이 있는지 확인한다. - 집품한 상품이 없음")
    void hasPickedItem_not_picked() {
        final Picking picking = new Picking();

        final boolean hasPickedItem = picking.hasPickedItem();

        assertThat(hasPickedItem).isFalse();
    }
}