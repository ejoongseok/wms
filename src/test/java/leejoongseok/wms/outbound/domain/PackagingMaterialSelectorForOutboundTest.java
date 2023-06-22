package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemSize;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PackagingMaterialSelectorForOutboundTest {

    private PackagingMaterialSelectorForOutbound packagingMaterialSelectorForOutbound;

    private static OrderItem createOrderItem(
            final ItemSize itemSize,
            final int orderQuantity,
            final int itemWeightInGrams) {
        final Item item = Instancio
                .of(Item.class)
                .supply(Select.field(Item::getItemSize),
                        () -> itemSize)
                .supply(Select.field(Item::getWeightInGrams),
                        () -> itemWeightInGrams)
                .create();
        return Instancio.of(OrderItem.class)
                .supply(Select.field(OrderItem::getItem),
                        () -> item)
                .supply(Select.field(OrderItem::getOrderQuantity),
                        () -> orderQuantity)
                .create();
    }

    @BeforeEach
    void setUp() {
        final List<PackagingMaterial> packagingMaterials = List.of(
                createPackagingMaterial(
                        createPackagingMaterialDimension(
                                100,
                                100,
                                100,
                                1
                        ),
                        1000,
                        "포장자재1(최대무게 1키로)"),
                createPackagingMaterial(
                        createPackagingMaterialDimension(
                                200,
                                200,
                                200,
                                10
                        ),
                        5000,
                        "포장자재2(최대무게 5키로)")
        );
        packagingMaterialSelectorForOutbound =
                new PackagingMaterialSelectorForOutbound(packagingMaterials);
    }

    private PackagingMaterial createPackagingMaterial(
            final PackagingMaterialDimension packagingMaterialDimension,
            final int maxWeightInGrams,
            final String packagingMaterialName) {
        return Instancio.of(PackagingMaterial.class)
                .supply(Select.field(PackagingMaterial::getPackagingMaterialDimension),
                        () -> packagingMaterialDimension)
                .supply(Select.field(PackagingMaterial::getMaxWeightInGrams),
                        () -> maxWeightInGrams)
                .supply(Select.field(PackagingMaterial::getName),
                        () -> packagingMaterialName)
                .create();
    }

    private PackagingMaterialDimension createPackagingMaterialDimension(
            final int innerWidthMillimeter,
            final int innerHeightMillimeter,
            final int innerLengthMillimeter,
            final int thicknessInMillimeter) {
        return Instancio.of(PackagingMaterialDimension.class)
                .supply(Select.field(PackagingMaterialDimension::getInnerWidthMillimeter),
                        () -> innerWidthMillimeter)
                .supply(Select.field(PackagingMaterialDimension::getInnerHeightMillimeter),
                        () -> innerHeightMillimeter)
                .supply(Select.field(PackagingMaterialDimension::getInnerLengthMillimeter),
                        () -> innerLengthMillimeter)
                .supply(Select.field(PackagingMaterialDimension::getThicknessInMillimeter),
                        () -> thicknessInMillimeter)
                .create();
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장 자재1 선택")
    void select() {
        final int widthMillimeter = 10;
        final int lengthMillimeter = 10;
        final int heightMillimeter = 10;
        final int orderQuantity = 10;
        final int itemWeightInGrams = 100;
        final List<OrderItem> orderItems = List.of(
                createOrderItem(
                        createItemSize(
                                widthMillimeter,
                                lengthMillimeter,
                                heightMillimeter
                        ),
                        orderQuantity,
                        itemWeightInGrams
                )
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final Optional<PackagingMaterial> packagingMaterial =
                packagingMaterialSelectorForOutbound.select(
                        orderItems,
                        cushioningMaterialVolume,
                        cushioningMaterialWeightInGrams
                );

        assertThat(packagingMaterial).isPresent();
        assertThat(packagingMaterial.get().getName()).isEqualTo("포장자재1(최대무게 1키로)");
    }

    private ItemSize createItemSize(
            final int widthMillimeter,
            final int lengthMillimeter,
            final int heightMillimeter) {
        return new ItemSize(
                widthMillimeter,
                lengthMillimeter,
                heightMillimeter
        );
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장 자재2 선택 (포장자재1은 최대무게 1키로이므로 무게 100그램 짜리 상품 11개를 포장할 수 없다.)")
    void select2() {
        final int widthMillimeter = 10;
        final int lengthMillimeter = 10;
        final int heightMillimeter = 10;
        final int orderQuantity = 11;
        final int itemWeightInGrams = 100;
        final List<OrderItem> orderItems = List.of(
                createOrderItem(
                        createItemSize(
                                widthMillimeter,
                                lengthMillimeter,
                                heightMillimeter
                        ),
                        orderQuantity,
                        itemWeightInGrams
                )
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final Optional<PackagingMaterial> packagingMaterial =
                packagingMaterialSelectorForOutbound.select(
                        orderItems,
                        cushioningMaterialVolume,
                        cushioningMaterialWeightInGrams
                );

        assertThat(packagingMaterial).isPresent();
        assertThat(packagingMaterial.get().getName()).isEqualTo("포장자재2(최대무게 5키로)");
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장 자재2 선택 (포장자재1은 최대 부피는 100x100x100 이므로 부피 100x100x100 짜리 상품 2개를 포장할 수 없다.)")
    void select2_() {
        final int widthMillimeter = 100;
        final int lengthMillimeter = 100;
        final int heightMillimeter = 100;
        final int orderQuantity = 2;
        final int itemWeightInGrams = 1;
        final List<OrderItem> orderItems = List.of(
                createOrderItem(
                        createItemSize(
                                widthMillimeter,
                                lengthMillimeter,
                                heightMillimeter
                        ),
                        orderQuantity,
                        itemWeightInGrams
                )
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final Optional<PackagingMaterial> packagingMaterial =
                packagingMaterialSelectorForOutbound.select(
                        orderItems,
                        cushioningMaterialVolume,
                        cushioningMaterialWeightInGrams
                );

        assertThat(packagingMaterial).isPresent();
        assertThat(packagingMaterial.get().getName()).isEqualTo("포장자재2(최대무게 5키로)");
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장가능한 자재가 없음(최대 무게 초과)")
    void select_empty() {
        final int widthMillimeter = 100;
        final int lengthMillimeter = 100;
        final int heightMillimeter = 100;
        final int orderQuantity = 2;
        final int itemWeightInGrams = 10000;
        final List<OrderItem> orderItems = List.of(
                createOrderItem(
                        createItemSize(
                                widthMillimeter,
                                lengthMillimeter,
                                heightMillimeter
                        ),
                        orderQuantity,
                        itemWeightInGrams
                )
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final Optional<PackagingMaterial> packagingMaterial =
                packagingMaterialSelectorForOutbound.select(
                        orderItems,
                        cushioningMaterialVolume,
                        cushioningMaterialWeightInGrams
                );

        assertThat(packagingMaterial).isEmpty();
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장가능한 자재가 없음(최대 부피 초과)")
    void select_empty2() {
        final int widthMillimeter = 200;
        final int lengthMillimeter = 200;
        final int heightMillimeter = 200;
        final int orderQuantity = 2;
        final int itemWeightInGrams = 1;
        final List<OrderItem> orderItems = List.of(
                createOrderItem(
                        createItemSize(
                                widthMillimeter,
                                lengthMillimeter,
                                heightMillimeter
                        ),
                        orderQuantity,
                        itemWeightInGrams
                )
        );
        final Integer cushioningMaterialVolume = 0;
        final Integer cushioningMaterialWeightInGrams = 0;

        final Optional<PackagingMaterial> packagingMaterial =
                packagingMaterialSelectorForOutbound.select(
                        orderItems,
                        cushioningMaterialVolume,
                        cushioningMaterialWeightInGrams
                );

        assertThat(packagingMaterial).isEmpty();
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. ")
    void select_outbound() {
        final Integer itemLengthMillimeter = 10;
        final Integer itemWidthMillimeter = 10;
        final Integer itemHeightMillimeter = 10;
        final Integer itemWeightInGrams = 100;
        final Item item = createItemWithItemSize(
                itemLengthMillimeter,
                itemWidthMillimeter,
                itemHeightMillimeter,
                itemWeightInGrams);
        final Integer outboundQuantity = 1;
        final OutboundItem outboundItem = createOutboundWithItemOrQuantity(
                item,
                outboundQuantity);
        final Outbound outbound = createOutboundWithCushioningMaterial(
                CushioningMaterial.AIR_PILLOW,
                1,
                outboundItem
        );

        final PackagingMaterial packagingMaterial =
                packagingMaterialSelectorForOutbound.select(outbound);

        assertThat(packagingMaterial.getName()).isEqualTo("포장자재1(최대무게 1키로)");
    }

    private Item createItemWithItemSize(
            final Integer itemLengthMillimeter,
            final Integer itemWidthMillimeter,
            final Integer itemHeightMillimeter,
            final Integer itemWeightInGrams) {
        final ItemSize itemSize = Instancio.of(ItemSize.class)
                .supply(Select.field(ItemSize::getLengthMillimeter),
                        () -> itemLengthMillimeter)
                .supply(Select.field(ItemSize::getWidthMillimeter),
                        () -> itemWidthMillimeter)
                .supply(Select.field(ItemSize::getHeightMillimeter),
                        () -> itemHeightMillimeter)
                .create();
        return Instancio.of(Item.class)
                .supply(Select.field(Item::getItemSize),
                        () -> itemSize)
                .supply(Select.field(Item::getWeightInGrams),
                        () -> itemWeightInGrams)
                .create();
    }

    private OutboundItem createOutboundWithItemOrQuantity(
            final Item item,
            final Integer outboundQuantity) {
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getOutboundQuantity),
                        () -> outboundQuantity)
                .supply(Select.field(OutboundItem::getItem),
                        () -> item)
                .create();
    }

    private Outbound createOutboundWithCushioningMaterial(
            final CushioningMaterial cushioningMaterial,
            final Integer cushioningMaterialQuantity,
            final OutboundItem outboundItem) {
        return Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getCushioningMaterial),
                        () -> cushioningMaterial)
                .supply(Select.field(Outbound::getCushioningMaterialQuantity),
                        () -> cushioningMaterialQuantity)
                .supply(Select.field(Outbound::getOutboundItems),
                        () -> List.of(outboundItem))
                .ignore(Select.field(Outbound::getRecommendedPackagingMaterial))
                .create();
    }

    @Test
    @DisplayName("출고에 사용할 포장재를 선택한다. 포장자재 2선택 (무게 1.1kg)")
    void select_outbound2() {
        final Integer itemLengthMillimeter = 10;
        final Integer itemWidthMillimeter = 10;
        final Integer itemHeightMillimeter = 10;
        final Integer itemWeightInGrams = 100;
        final Item item = createItemWithItemSize(
                itemLengthMillimeter,
                itemWidthMillimeter,
                itemHeightMillimeter,
                itemWeightInGrams);
        final Integer outboundQuantity = 11;
        final OutboundItem outboundItem = createOutboundWithItemOrQuantity(
                item,
                outboundQuantity);
        final Outbound outbound = createOutboundWithCushioningMaterial(
                CushioningMaterial.NONE,
                0,
                outboundItem
        );

        final PackagingMaterial packagingMaterial =
                packagingMaterialSelectorForOutbound.select(outbound);

        assertThat(packagingMaterial.getName()).isEqualTo("포장자재2(최대무게 5키로)");
    }
}