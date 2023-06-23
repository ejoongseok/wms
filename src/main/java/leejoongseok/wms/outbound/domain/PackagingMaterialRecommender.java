package leejoongseok.wms.outbound.domain;

import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 출고에 사용할 포장 자재를 선택하고 없으면 Optional.empty()를 반환한다.
 */
public class PackagingMaterialRecommender {
    private final List<PackagingMaterial> packagingMaterials;

    public PackagingMaterialRecommender(
            final List<PackagingMaterial> packagingMaterials) {
        Assert.notEmpty(packagingMaterials, "포장 자재 목록이 비어있습니다.");
        this.packagingMaterials = packagingMaterials;
    }

    /**
     * 완충재를 포함한 주문상품 목록을 출고할 수 있는
     * 포장 자재를 선택하고 없으면 Optional.empty()를 반환한다.
     * 포장가능한 포장재 중 부피가 가장 작은것을 추천해줌.
     */
    public Optional<PackagingMaterial> recommend(
            final List<OrderItem> orderItems,
            final Integer cushioningMaterialVolume,
            final Integer cushioningMaterialWeightInGrams) {
        validateParameter(
                orderItems,
                cushioningMaterialVolume,
                cushioningMaterialWeightInGrams);

        final Long totalVolume = calculateTotalVolume(
                orderItems,
                cushioningMaterialVolume);
        final Long totalWeightInGrams = calculateTotalWeightInGrams(
                orderItems,
                cushioningMaterialWeightInGrams);
        return findPerfectPackagingMaterial(
                totalVolume,
                totalWeightInGrams);
    }

    private void validateParameter(
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

    private Long calculateTotalVolume(
            final List<OrderItem> orderItems,
            final Integer cushioningMaterialVolume) {
        final Long itemTotalVolume = orderItems.stream()
                .mapToLong(orderItem -> orderItem.calculateTotalVolume())
                .sum();
        return itemTotalVolume + cushioningMaterialVolume;
    }

    private Long calculateTotalWeightInGrams(
            final List<OrderItem> orderItems,
            final Integer cushioningMaterialWeightInGrams) {
        final Long itemTotalWeightInGrams = orderItems.stream()
                .mapToLong(OrderItem::calculateTotalWeightInGrams)
                .sum();
        return itemTotalWeightInGrams + cushioningMaterialWeightInGrams;
    }

    /**
     * 포장 가능한 포장재 중 부피가 가장 작은것을 추천해줌.
     */
    private Optional<PackagingMaterial> findPerfectPackagingMaterial(
            final Long totalVolume,
            final Long totalWeightInGrams) {
        return packagingMaterials.stream()
                .filter(pm -> pm.isPackageable(totalVolume, totalWeightInGrams))
                .min(Comparator.comparingLong(
                        PackagingMaterial::calculatePackageableVolume));
    }

    /**
     * 출고에 사용할 포장 자재를 선택하고 없으면
     * IllegalArgumentException을 발생시킨다.
     * 포장가능한 포장재 중 부피가 가장 작은것을 추천해줌.
     */
    public PackagingMaterial recommend(final Outbound outbound) {
        Assert.notNull(outbound, "포장재를 추천할 출고는 필수입니다.");
        final Long totalVolume = outbound.calculateTotalVolume();
        final Long totalWeightInGrams = outbound.calculateTotalWeightInGrams();
        return findPerfectPackagingMaterial(totalVolume, totalWeightInGrams)
                .orElseThrow(() -> new IllegalArgumentException(
                        "포장 가능한 포장재가 없습니다."));
    }
}
