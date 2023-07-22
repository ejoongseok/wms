package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.outbound.order.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static leejoongseok.wms.common.fixture.ItemFixture.aItem;
import static leejoongseok.wms.common.fixture.ItemSizeFixture.aItemSize;
import static leejoongseok.wms.common.fixture.OrderItemFixture.aOrderItem;
import static leejoongseok.wms.common.fixture.OutboundFixture.aOutboundWithNoRecommendedPackagingMaterial;
import static leejoongseok.wms.common.fixture.OutboundItemFixture.aOutboundItem;
import static leejoongseok.wms.common.fixture.PackagingMaterialDimensionFixture.aPackagingMaterialDimension;
import static leejoongseok.wms.common.fixture.PackagingMaterialFixture.aPackagingMaterial;
import static org.assertj.core.api.Assertions.assertThat;

class PackagingMaterialRecommenderTest {

    final List<PackagingMaterial> packagingMaterials = List.of(
            createPackagingMaterial(
                    aPackagingMaterialDimension()
                            .withInnerWidthInMillimeters(100)
                            .withInnerHeightInMillimeters(100)
                            .withInnerLengthInMillimeters(100)
                            .withThicknessInMillimeters(1)
                            .build(),
                    1000,
                    "포장자재1(최대무게 1키로)"),
            createPackagingMaterial(
                    aPackagingMaterialDimension()
                            .withInnerWidthInMillimeters(200)
                            .withInnerHeightInMillimeters(200)
                            .withInnerLengthInMillimeters(200)
                            .withThicknessInMillimeters(10)
                            .build(),
                    5000,
                    "포장자재2(최대무게 5키로)")
    );

    private PackagingMaterial createPackagingMaterial(
            final PackagingMaterialDimension packagingMaterialDimension,
            final int maxWeightInGrams,
            final String packagingMaterialName) {
        return aPackagingMaterial()
                .withPackagingMaterialDimension(packagingMaterialDimension)
                .withMaxWeightInGrams(maxWeightInGrams)
                .withName(packagingMaterialName)
                .build();
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장 자재1 선택")
    void select() {
        final List<OrderItem> orderItems = List.of(
                aOrderItem()
                        .withItem(aItem()
                                .withItemSize(aItemSize()
                                        .withWidthInMillimeter(10)
                                        .withHeightInMillimeter(10)
                                        .withLengthInMillimeter(10)
                                        .build())
                                .withWeightInGrams(100)
                                .build())
                        .withOrderQuantity(10)
                        .build()
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final OrderPackagingMaterialRecommender recommender = new OrderPackagingMaterialRecommender(
                packagingMaterials,
                orderItems,
                cushioningMaterialVolume,
                cushioningMaterialWeightInGrams);
        final Optional<PackagingMaterial> packagingMaterial = recommender.recommendPackagingMaterial();

        assertThat(packagingMaterial).isPresent();
        assertThat(packagingMaterial.get().getName()).isEqualTo("포장자재1(최대무게 1키로)");
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장 자재2 선택 (포장자재1은 최대무게 1키로이므로 무게 100그램 짜리 상품 11개를 포장할 수 없다.)")
    void select2() {
        final List<OrderItem> orderItems = List.of(
                aOrderItem()
                        .withItem(aItem()
                                .withItemSize(aItemSize()
                                        .withWidthInMillimeter(10)
                                        .withHeightInMillimeter(10)
                                        .withLengthInMillimeter(10)
                                        .build())
                                .withWeightInGrams(100)
                                .build())
                        .withOrderQuantity(11)
                        .build()
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final OrderPackagingMaterialRecommender recommender = new OrderPackagingMaterialRecommender(
                packagingMaterials,
                orderItems,
                cushioningMaterialVolume,
                cushioningMaterialWeightInGrams);
        final Optional<PackagingMaterial> packagingMaterial = recommender.recommendPackagingMaterial();

        assertThat(packagingMaterial).isPresent();
        assertThat(packagingMaterial.get().getName()).isEqualTo("포장자재2(최대무게 5키로)");
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장 자재2 선택 (포장자재1은 최대 부피는 100x100x100 이므로 부피 100x100x100 짜리 상품 2개를 포장할 수 없다.)")
    void select2_() {
        final List<OrderItem> orderItems = List.of(
                aOrderItem()
                        .withItem(aItem()
                                .withItemSize(aItemSize()
                                        .withWidthInMillimeter(100)
                                        .withHeightInMillimeter(100)
                                        .withLengthInMillimeter(100)
                                        .build())
                                .withWeightInGrams(1)
                                .build())
                        .withOrderQuantity(2)
                        .build()
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final OrderPackagingMaterialRecommender recommender = new OrderPackagingMaterialRecommender(
                packagingMaterials,
                orderItems,
                cushioningMaterialVolume,
                cushioningMaterialWeightInGrams);
        final Optional<PackagingMaterial> packagingMaterial = recommender.recommendPackagingMaterial();

        assertThat(packagingMaterial).isPresent();
        assertThat(packagingMaterial.get().getName()).isEqualTo("포장자재2(최대무게 5키로)");
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장가능한 자재가 없음(최대 무게 초과)")
    void select_empty() {
        final List<OrderItem> orderItems = List.of(
                aOrderItem()
                        .withItem(aItem()
                                .withItemSize(aItemSize()
                                        .withWidthInMillimeter(100)
                                        .withHeightInMillimeter(100)
                                        .withLengthInMillimeter(100)
                                        .build())
                                .withWeightInGrams(10000)
                                .build())
                        .withOrderQuantity(2)
                        .build()
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final OrderPackagingMaterialRecommender recommender = new OrderPackagingMaterialRecommender(
                packagingMaterials,
                orderItems,
                cushioningMaterialVolume,
                cushioningMaterialWeightInGrams);
        final Optional<PackagingMaterial> packagingMaterial = recommender.recommendPackagingMaterial();

        assertThat(packagingMaterial).isEmpty();
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장가능한 자재가 없음(최대 부피 초과)")
    void select_empty2() {
        final List<OrderItem> orderItems = List.of(
                aOrderItem()
                        .withItem(aItem()
                                .withItemSize(aItemSize()
                                        .withWidthInMillimeter(200)
                                        .withHeightInMillimeter(200)
                                        .withLengthInMillimeter(200)
                                        .build())
                                .withWeightInGrams(1)
                                .build())
                        .withOrderQuantity(2)
                        .build()
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final OrderPackagingMaterialRecommender recommender = new OrderPackagingMaterialRecommender(
                packagingMaterials,
                orderItems,
                cushioningMaterialVolume,
                cushioningMaterialWeightInGrams);
        final Optional<PackagingMaterial> packagingMaterial = recommender.recommendPackagingMaterial();

        assertThat(packagingMaterial).isEmpty();
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. ")
    void select_outbound() {
        final OutboundItem outboundItem = aOutboundItem()
                .withOutboundQuantity(1)
                .withItem(aItem()
                        .withItemSize(aItemSize()
                                .withWidthInMillimeter(10)
                                .withHeightInMillimeter(10)
                                .withLengthInMillimeter(10)
                                .build())
                        .withWeightInGrams(100)
                        .build())
                .build();
        final Outbound outbound = aOutboundWithNoRecommendedPackagingMaterial()
                .withCushioningMaterial(CushioningMaterial.AIR_PILLOW)
                .withCushioningMaterialQuantity(1)
                .withOutboundItems(List.of(outboundItem))
                .build();

        final PackagingMaterial packagingMaterial =
                new OutboundPackagingMaterialRecommender(packagingMaterials, outbound).recommendPackagingMaterial().get();

        assertThat(packagingMaterial.getName()).isEqualTo("포장자재1(최대무게 1키로)");
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장자재 2선택 (무게 1.1kg)")
    void select_outbound2() {
        final OutboundItem outboundItem = aOutboundItem()
                .withOutboundQuantity(11)
                .withItem(aItem()
                        .withItemSize(aItemSize()
                                .withWidthInMillimeter(10)
                                .withHeightInMillimeter(10)
                                .withLengthInMillimeter(10)
                                .build())
                        .withWeightInGrams(100)
                        .build())
                .build();
        final Outbound outbound = aOutboundWithNoRecommendedPackagingMaterial()
                .withCushioningMaterial(CushioningMaterial.NONE)
                .withCushioningMaterialQuantity(0)
                .withOutboundItems(List.of(outboundItem))
                .build();

        final PackagingMaterial packagingMaterial =
                new OutboundPackagingMaterialRecommender(packagingMaterials, outbound).recommendPackagingMaterial().get();

        assertThat(packagingMaterial.getName()).isEqualTo("포장자재2(최대무게 5키로)");
    }
}