package leejoongseok.wms.outbound.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import leejoongseok.wms.outbound.exception.OutboundItemIdNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Outbound는 현실세계에서 출고 요청부터 출고 완료까지의 과정을 전산화로 표현한 것이다.
 * 출고의 과정은 출고 대기, 집품, 검수, 포장, 출고 완료의 단계로 나뉜다.
 */
@Entity
@Table(name = "outbound")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("출고")
public class Outbound {

    @OneToMany(mappedBy = "outbound", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private final List<OutboundItem> outboundItems = new ArrayList<>();
    @Column(name = "order_id", nullable = false)
    @Comment("주문 ID")
    private Long orderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_packaging_material_id", nullable = true)
    @Comment("추천 포장재 ID")
    @Getter
    private PackagingMaterial recommendedPackagingMaterial;
    @Embedded
    private OutboundCustomer outboundCustomer;
    @Enumerated(EnumType.STRING)
    @Column(name = "cushioning_material", nullable = false)
    @Comment("완충재")
    @Getter(AccessLevel.PROTECTED)
    private CushioningMaterial cushioningMaterial;
    @Column(name = "cushioning_material_quantity", nullable = false)
    @Comment("완충재 수량")
    @Getter(AccessLevel.PROTECTED)
    private Integer cushioningMaterialQuantity;
    @Column(name = "is_priority_delivery", nullable = false)
    @Comment("우선 배송 여부")
    private Boolean priorityDelivery;
    @Column(name = "desired_delivery_date", nullable = false)
    @Comment("희망 배송일")
    private LocalDate desiredDeliveryDate;
    @Column(name = "outbound_requirements", nullable = true)
    @Comment("출고 요구사항")
    private String outboundRequirements;
    @Column(name = "delivery_requirements", nullable = true)
    @Comment("배송 요구사항")
    private String deliveryRequirements;
    @Column(name = "ordered_at", nullable = false)
    @Comment("주문 일시")
    private LocalDateTime orderedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "outbound_status", nullable = false)
    @Comment("출고 상태")
    @Getter(AccessLevel.PROTECTED)
    private final OutboundStatus outboundStatus = OutboundStatus.READY;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("출고 ID")
    @Getter(AccessLevel.PROTECTED)
    private Long id;

    public Outbound(
            final Long orderId,
            final PackagingMaterial recommendedPackagingMaterial,
            final String customerAddress,
            final String customerName,
            final String customerEmail,
            final String customerPhoneNumber,
            final String customerZipCode,
            final CushioningMaterial cushioningMaterial,
            final Integer cushioningMaterialQuantity,
            final Boolean priorityDelivery,
            final LocalDate desiredDeliveryDate,
            final String outboundRequirements,
            final String deliveryRequirements,
            final LocalDateTime orderedAt) {
        validateConstructor(
                orderId,
                cushioningMaterial,
                cushioningMaterialQuantity,
                priorityDelivery,
                desiredDeliveryDate,
                orderedAt);
        this.orderId = orderId;
        this.recommendedPackagingMaterial = recommendedPackagingMaterial;
        outboundCustomer = new OutboundCustomer(
                customerAddress,
                customerName,
                customerEmail,
                customerPhoneNumber,
                customerZipCode);
        this.cushioningMaterial = cushioningMaterial;
        this.cushioningMaterialQuantity = cushioningMaterialQuantity;
        this.priorityDelivery = priorityDelivery;
        this.desiredDeliveryDate = desiredDeliveryDate;
        this.outboundRequirements = outboundRequirements;
        this.deliveryRequirements = deliveryRequirements;
        this.orderedAt = orderedAt;

    }

    private void validateConstructor(
            final Long orderId,
            final CushioningMaterial cushioningMaterial,
            final Integer cushioningMaterialQuantity,
            final Boolean priorityDelivery,
            final LocalDate desiredDeliveryDate,
            final LocalDateTime orderedAt) {
        Assert.notNull(orderId, "주문 ID는 필수입니다.");
        Assert.notNull(cushioningMaterial, "완충재는 필수입니다.");
        Assert.notNull(cushioningMaterialQuantity, "완충재 수량은 필수입니다.");
        if (0 > cushioningMaterialQuantity) {
            throw new IllegalArgumentException("완충재 수량은 0 이상이어야 합니다.");
        }
        if (CushioningMaterial.NONE == cushioningMaterial
                && 0 < cushioningMaterialQuantity) {
            throw new IllegalArgumentException("완충재가 없는 경우 완충재 수량은 0이어야 합니다.");
        }
        Assert.notNull(priorityDelivery, "우선 배송 여부는 필수입니다.");
        Assert.notNull(desiredDeliveryDate, "희망 배송일은 필수입니다.");
        Assert.notNull(orderedAt, "주문 일시는 필수입니다.");
    }

    public void addOutboundItem(final OutboundItem outboundItem) {
        outboundItems.add(outboundItem);
        outboundItem.assignOutbound(this);
    }

    /**
     * 출고를 분할한다.
     * 출고를 분할하기 위해서는 출고는 반드시 대기 상태여야 한다.
     * 분할한 뒤 기존 출고의 상품은 하나 이상 남아 있어야 한다. (기존의 출고가 삭제되면 안됨.)
     * 출고를 분할한 뒤 기존의 출고 목록 중 출고해야할 상품의 수량이 0인 경우 해당 출고 상품을 목록에서 제거한다.
     */
    public Outbound split(
            final List<OutboundItemToSplit> outboundItemToSplits) {
        validateSplit(outboundItemToSplits);
        final List<OutboundItem> splitOutboundItems = splitOutboundItems(
                outboundItemToSplits);
        final Outbound cloneNewOutbound = cloneNewOutbound(splitOutboundItems);
        decreaseQuantityBySplitQuantity(outboundItemToSplits);
        clearEmptyOutboundItemsAfterSplit();
        return cloneNewOutbound;
    }

    /**
     * 분할한 만큼 현재 출고 상품의 수량을 감소시킨다.
     */
    private void decreaseQuantityBySplitQuantity(
            final List<OutboundItemToSplit> outboundItemToSplits) {
        for (final OutboundItemToSplit outboundItemToSplit : outboundItemToSplits) {
            final OutboundItem outboundItem = getOutboundItem(outboundItemToSplit.getOutboundItemId());
            outboundItem.decreaseQuantity(outboundItemToSplit.getQuantityOfSplit());
        }
    }

    /**
     * 출고를 분할 할 수 있는지 검증한다.
     * 출고를 분할하기 위해서는 출고는 반드시 대기 상태여야 한다.
     * 분할한 뒤 기존 출고의 상품이 하나도 남아있지 않으면 안된다.
     */
    private void validateSplit(
            final List<OutboundItemToSplit> outboundItemToSplits) {
        if (outboundStatus != OutboundStatus.READY) {
            throw new IllegalStateException(
                    "출고는 대기 상태에서만 분할 할 수 있습니다.");
        }
        Assert.notEmpty(outboundItemToSplits,
                "분할 대상 출고 상품은 1개 이상이어야 합니다.");
        if (outboundItemToSplits.size() > outboundItems.size()) {
            throw new IllegalArgumentException(
                    "분할 대상 출고 상품은 출고 상품의 개수보다 많을 수 없습니다.");
        }
        final int totalQuantityOfSplit = outboundItemToSplits.stream()
                .mapToInt(OutboundItemToSplit::getQuantityOfSplit)
                .sum();
        final int totalQuantityOfOutboundItem = outboundItems.stream()
                .mapToInt(OutboundItem::getOutboundQuantity)
                .sum();
        if (totalQuantityOfSplit >= totalQuantityOfOutboundItem) {
            throw new IllegalArgumentException(
                    """
                            분할하려는 상품의 총 수량은 출고 상품의 총 수량보다 작아야 합니다.
                            분할하려는 상품의 총 수량: %d, 출고 상품의 총 수량: %d
                            """
                            .formatted(
                                    totalQuantityOfSplit,
                                    totalQuantityOfOutboundItem));
        }
    }

    private List<OutboundItem> splitOutboundItems(
            final List<OutboundItemToSplit> outboundItemToSplits) {
        return outboundItemToSplits.stream()
                .map(outboundItemToSplit ->
                        getOutboundItem(outboundItemToSplit.getOutboundItemId())
                                .split(outboundItemToSplit.getQuantityOfSplit()))
                .toList();
    }

    private OutboundItem getOutboundItem(
            final Long outboundItemId) {
        return outboundItems.stream()
                .filter(outboundItem -> outboundItem.getId().equals(outboundItemId))
                .findFirst()
                .orElseThrow(() -> new OutboundItemIdNotFoundException(outboundItemId));
    }

    /**
     * 출고의 정보는 똑같이 생성하고
     * 분할한 출고 상품 목록을 새로운 출고에 추가한다.
     */
    private Outbound cloneNewOutbound(
            final List<OutboundItem> splitOutboundItems) {
        final Outbound outbound = new Outbound(
                orderId,
                recommendedPackagingMaterial,
                outboundCustomer.getCustomerAddress(),
                outboundCustomer.getCustomerName(),
                outboundCustomer.getCustomerEmail(),
                outboundCustomer.getCustomerPhoneNumber(),
                outboundCustomer.getCustomerZipCode(),
                cushioningMaterial,
                cushioningMaterialQuantity,
                priorityDelivery,
                desiredDeliveryDate,
                outboundRequirements,
                deliveryRequirements,
                orderedAt);
        splitOutboundItems.forEach(outbound::addOutboundItem);
        return outbound;
    }

    /**
     * 출고를 분할한 뒤 기존 출고 목록 중
     * 출고해야할 상품의 수량이 0인 경우 해당
     * 출고 상품을 목록에서 제거한다.
     */
    private void clearEmptyOutboundItemsAfterSplit() {
        final List<OutboundItem> emptyOutboundItems = outboundItems.stream()
                .filter(OutboundItem::isZeroQuantity)
                .toList();
        outboundItems.removeAll(emptyOutboundItems);
    }

    public void assignRecommendedPackagingMaterial(
            final PackagingMaterial packagingMaterial) {
        recommendedPackagingMaterial = packagingMaterial;
    }

    /**
     * 출고의 총 부피는 출고 상품의 총 부피와 완충재의 총 부피의 합이다.
     */
    public Long calculateTotalVolume() {
        final Long itemTotalVolume = outboundItems.stream()
                .mapToLong(OutboundItem::calculateVolume)
                .sum();
        final Integer cushioningMaterialTotalVolume =
                cushioningMaterial.calculateTotalVolume(cushioningMaterialQuantity);
        return itemTotalVolume + cushioningMaterialTotalVolume;
    }

    /**
     * 출고의 총 무게는 출고 상품의 총 무게와 완충재의 총 무게와 포장재 무게의 합이다.
     */
    public Long calculateTotalWeightInGrams() {
        final long itemTotalWeightInGrams = outboundItems.stream()
                .mapToLong(OutboundItem::calculateWeightInGrams)
                .sum();
        final int cushioningMaterialTotalWeightInGrams =
                cushioningMaterial.calculateTotalWeightInGrams(cushioningMaterialQuantity);
        final Integer packagingMaterialWeightInGrams = null == recommendedPackagingMaterial
                ? 0
                : recommendedPackagingMaterial.getWeightInGrams();
        return itemTotalWeightInGrams + cushioningMaterialTotalWeightInGrams + packagingMaterialWeightInGrams;
    }
}
