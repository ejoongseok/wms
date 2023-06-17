package leejoongseok.wms.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

/**
 * 상품은 물류세터의 입고, 출고, 재고 등 모든 곳에서 사용되는 원천 데이터이다.
 * 추후 오픈마켓용 아이템과 물류용 아이템을 관리하는 아이템 관리 시스템으로 확장될 수 있다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
@Comment("상품")
public class Item {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("상품 ID")
    private Long id;
    @Column(name = "item_name", nullable = false)
    @Comment("상품명")
    private String itemName;
    @Column(name = "item_barcode", nullable = false)
    @Comment("상품 바코드")
    private String itemBarcode;
    @Column(name = "description")
    @Comment("설명")
    private String description;
    @Column(name = "brand", nullable = false)
    @Comment("브랜드명")
    private String brand;
    @Column(name = "maker", nullable = false)
    @Comment("제조사명")
    private String maker;
    @Embedded
    @Getter
    private ItemSize itemSize;
    @Column(name = "weight_gram", nullable = false)
    @Comment("무게 (g)")
    @Getter
    private Integer weightInGrams;
    @Column(name = "temperature_zone", nullable = false)
    @Comment("보관 온도")
    @Enumerated(EnumType.STRING)
    private TemperatureZone temperatureZone;
    @Column(name = "category", nullable = false)
    @Comment("카테고리")
    @Enumerated(EnumType.STRING)
    private Category category;

    public Item(
            final String itemName,
            final String itemBarcode,
            final String description,
            final String brandName,
            final String makerName,
            final ItemSize itemSize,
            final Integer weightInGrams,
            final TemperatureZone temperatureZone,
            final Category category) {
        validateConstructor(
                itemName,
                itemBarcode,
                brandName,
                makerName,
                itemSize,
                weightInGrams,
                temperatureZone,
                category);
        this.itemName = itemName;
        this.itemBarcode = itemBarcode;
        this.description = description;
        brand = brandName;
        maker = makerName;
        this.itemSize = itemSize;
        this.weightInGrams = weightInGrams;
        this.temperatureZone = temperatureZone;
        this.category = category;
    }

    private void validateConstructor(
            final String itemName,
            final String itemBarcode,
            final String brandName,
            final String makerName,
            final ItemSize itemSize,
            final Integer weightInGrams,
            final TemperatureZone temperatureZone,
            final Category category) {
        Assert.hasText(itemName, "상품명 필수입니다.");
        Assert.hasText(itemBarcode, "상품 바코드는 필수입니다.");
        Assert.hasText(brandName, "상품의 브랜드명은 필수입니다.");
        Assert.hasText(makerName, "상품의 제조사명은 필수입니다.");
        Assert.notNull(itemSize, "상품의 크기는 필수입니다.");
        Assert.isTrue(null != weightInGrams && 0 < weightInGrams,
                "상품의 무게는 1g 이상이어야 합니다.");
        Assert.notNull(temperatureZone, "상품의 보관 온도는 필수입니다.");
        Assert.notNull(category, "상품의 카테고리는 필수입니다.");
    }

    public Long calculateVolume() {
        return itemSize.calculateVolume();
    }
}
