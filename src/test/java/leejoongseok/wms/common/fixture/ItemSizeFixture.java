package leejoongseok.wms.common.fixture;

import leejoongseok.wms.item.domain.ItemSize;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class ItemSizeFixture {

    private InstancioApi<ItemSize> itemSizeInstance = Instancio.of(ItemSize.class);

    public static ItemSizeFixture aItemSize() {
        return new ItemSizeFixture();
    }

    public ItemSizeFixture withWidthInMillimeter(final Integer widthInMillimeters) {
        itemSizeInstance.supply(Select.field(ItemSize::getWidthInMillimeters), () -> widthInMillimeters);
        return this;
    }

    public ItemSizeFixture withHeightInMillimeter(final Integer heightInMillimeters) {
        itemSizeInstance.supply(Select.field(ItemSize::getHeightInMillimeters), () -> heightInMillimeters);
        return this;
    }

    public ItemSizeFixture withLengthInMillimeter(final Integer lengthInMillimeters) {
        itemSizeInstance.supply(Select.field(ItemSize::getLengthInMillimeters), () -> lengthInMillimeters);
        return this;
    }

    public ItemSize build() {
        itemSizeInstance = null == itemSizeInstance ? Instancio.of(ItemSize.class) : itemSizeInstance;
        return itemSizeInstance.create();
    }
}
