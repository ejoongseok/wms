package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.outbound.order.OrderItem;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 주문에 사용할 포장 자재를 선택하고 없으면 Optional.empty()를 반환한다.
 */
public class OrderPackagingMaterialRecommender extends PackagingMaterialRecommender {
    private final List<OrderItem> orderItems;
    private final Integer cushioningMaterialVolume;
    private final Integer cushioningMaterialWeightInGrams;

    public OrderPackagingMaterialRecommender(
            final List<PackagingMaterial> packagingMaterials,
            final List<OrderItem> orderItems,
            final Integer cushioningMaterialVolume,
            final Integer cushioningMaterialWeightInGrams) {
        super(packagingMaterials);
        validateConstructor(
                packagingMaterials,
                orderItems,
                cushioningMaterialVolume,
                cushioningMaterialWeightInGrams);

        this.orderItems = orderItems;
        this.cushioningMaterialVolume = cushioningMaterialVolume;
        this.cushioningMaterialWeightInGrams = cushioningMaterialWeightInGrams;
    }

    private static void validateConstructor(
            final List<PackagingMaterial> packagingMaterials,
            final List<OrderItem> orderItems,
            final Integer cushioningMaterialVolume,
            final Integer cushioningMaterialWeightInGrams) {
        Assert.notEmpty(orderItems, "주문 상품 목록이 비어있습니다.");
        Assert.notEmpty(packagingMaterials, "포장 자재 목록이 비어있습니다.");
        Assert.notNull(cushioningMaterialVolume, "완충재 부피를 입력해 주세요.");
        Assert.notNull(cushioningMaterialWeightInGrams, "완충재 무게를 입력해 주세요.");
        if (0 > cushioningMaterialVolume) {
            throw new IllegalArgumentException("완충재 부피가 0보다 작을 수 없습니다.");
        }
        if (0 > cushioningMaterialWeightInGrams) {
            throw new IllegalArgumentException("완충재 무게가 0보다 작을 수 없습니다.");
        }
    }

    @Override
    protected Long calculateTotalVolume() {
        final Long itemTotalVolume = orderItems.stream()
                .mapToLong(orderItem -> orderItem.calculateTotalVolume())
                .sum();
        return itemTotalVolume + cushioningMaterialVolume;
    }

    @Override
    protected Long calculateTotalWeightInGrams() {
        final Long itemTotalWeightInGrams = orderItems.stream()
                .mapToLong(OrderItem::calculateTotalWeightInGrams)
                .sum();
        return itemTotalWeightInGrams + cushioningMaterialWeightInGrams;
    }

}
