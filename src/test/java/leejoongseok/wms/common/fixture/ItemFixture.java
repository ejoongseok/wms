package leejoongseok.wms.common.fixture;

import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemSize;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class ItemFixture {

    private InstancioApi<Item> itemInstance = Instancio.of(Item.class);

    public static ItemFixture aItem() {
        return new ItemFixture();
    }

    public static ItemFixture aDefaultItem() {
        return aItem().withId(1L);
    }

    public ItemFixture withId(final Long id) {
        itemInstance.supply(Select.field(Item::getId), () -> id);
        return this;
    }

    public ItemFixture withItemSize(final ItemSize itemSize) {
        itemInstance.supply(Select.field(Item::getItemSize), () -> itemSize);
        return this;
    }

    public ItemFixture withWeightInGrams(final Integer weightInGrams) {
        itemInstance.supply(Select.field(Item::getWeightInGrams), () -> weightInGrams);
        return this;
    }

    public Item build() {
        itemInstance = null == itemInstance ? Instancio.of(Item.class) : itemInstance;
        return itemInstance.create();
    }
}
