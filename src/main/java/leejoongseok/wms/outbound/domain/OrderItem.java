package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.item.domain.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class OrderItem {
    private final Item item;
    private final Integer orderQuantity;
    private BigDecimal unitPrice;


    public Long getItemId() {
        return item.getId();
    }

    public long calculateVolume() {
        return item.calculateVolume() * orderQuantity;
    }

    public long calculateWeightInGrams() {
        return (long) item.getWeightInGrams() * orderQuantity;
    }
}
