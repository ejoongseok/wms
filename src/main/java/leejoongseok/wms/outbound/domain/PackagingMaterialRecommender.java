package leejoongseok.wms.outbound.domain;

import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 출고에 사용할 포장 자재를 선택하고 없으면 Optional.empty()를 반환한다.
 */
public abstract class PackagingMaterialRecommender {
    private final List<PackagingMaterial> packagingMaterials;

    protected PackagingMaterialRecommender(
            final List<PackagingMaterial> packagingMaterials) {
        Assert.notEmpty(packagingMaterials, "포장 자재 목록이 비어있습니다.");
        this.packagingMaterials = packagingMaterials;
    }

    protected abstract Long calculateTotalVolume();

    protected abstract Long calculateTotalWeightInGrams();

    /**
     * 포장 가능한 포장재 중 부피가 가장 작은것을 추천해줌.
     */
    public Optional<PackagingMaterial> findPerfectPackagingMaterial() {
        return packagingMaterials.stream()
                .filter(pm -> pm.isPackageable(calculateTotalVolume(), calculateTotalWeightInGrams()))
                .min(Comparator.comparingLong(
                        PackagingMaterial::calculatePackageableVolume));
    }

}
