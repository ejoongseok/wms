package leejoongseok.wms.item.feature;

import leejoongseok.wms.ApiTest;
import leejoongseok.wms.Scenario;
import leejoongseok.wms.item.domain.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CreateItemTest extends ApiTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("상품을 등록한다.")
    void createItem() {
        new Scenario().createItem().request();

        assertThat(itemRepository.findByItemBarcode("itemBarcode")).isPresent();
    }
}
