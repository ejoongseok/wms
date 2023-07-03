package leejoongseok.wms.outbound.domain;

import org.springframework.util.Assert;

import java.util.List;

/**
 * 출고에 사용할 포장 자재를 선택하고 없으면 Optional.empty()를 반환한다.
 */
public class OutboundPackagingMaterialRecommender extends PackagingMaterialRecommender {
    private final Outbound outbound;

    public OutboundPackagingMaterialRecommender(
            final List<PackagingMaterial> packagingMaterials,
            final Outbound outbound) {
        super(packagingMaterials);
        Assert.notNull(outbound, "포장재를 추천할 출고는 필수입니다.");
        this.outbound = outbound;

    }

    @Override
    protected Long calculateTotalVolume() {
        return outbound.calculateTotalVolume();
    }

    @Override
    protected Long calculateTotalWeightInGrams() {
        return outbound.calculateTotalWeightInGrams();
    }

}
