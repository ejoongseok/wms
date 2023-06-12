package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.packaging.domain.PackagingMaterial;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PackagingMaterialSelectorForOutbound {
    private final List<PackagingMaterial> packagingMaterials;

    public PackagingMaterialSelectorForOutbound(
            final List<PackagingMaterial> packagingMaterials) {
        Assert.notEmpty(packagingMaterials, "포장 자재 목록이 비어있습니다.");
        this.packagingMaterials = packagingMaterials;
    }

    public Optional<PackagingMaterial> select(
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
        return packagingMaterials.stream()
                .filter(packagingMaterial -> packagingMaterial.calculatePackageableVolume() >= totalVolume)
                .filter(packagingMaterial -> packagingMaterial.getMaxWeightInGrams() >= totalWeightInGrams)
                .sorted(Comparator.comparingLong(PackagingMaterial::calculatePackageableVolume))
                .findFirst();
    }

    private Long calculateTotalVolume(
            final List<OrderItem> orderItems,
            final Integer cushioningMaterialVolume) {
        final Long totalVolume = orderItems.stream()
                .mapToLong(orderItem -> orderItem.calculateVolume())
                .sum();
        return totalVolume + cushioningMaterialVolume;
    }

    private Long calculateTotalWeightInGrams(
            final List<OrderItem> orderItems,
            final Integer cushioningMaterialWeightInGrams) {
        final Long totalWeightInGrams = orderItems.stream()
                .mapToLong(orderItem -> orderItem.calculateWeightInGrams())
                .sum();
        return totalWeightInGrams + cushioningMaterialWeightInGrams;
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
}
