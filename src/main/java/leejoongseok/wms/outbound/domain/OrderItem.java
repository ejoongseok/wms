package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.item.domain.Item;
import lombok.Getter;

@Getter
public class OrderItem {
    private Item item;
    private Integer orderQuantity;


    public Long getItemId() {
        return item.getId();
    }
}
