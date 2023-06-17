package leejoongseok.wms.outbound.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @Getter(AccessLevel.PROTECTED)
    private final List<OutboundItem> outboundItems = new ArrayList<>();
    @Column(name = "order_id", nullable = false)
    @Comment("주문 ID")
    private Long orderId;
    @Column(name = "recommended_packaging_material_id", nullable = true)
    @Comment("추천 포장재 ID")
    private Long recommendedPackagingMaterialId;
    @Embedded
    private OutboundCustomer outboundCustomer;
    @Enumerated(EnumType.STRING)
    @Column(name = "cushioning_material", nullable = false)
    @Comment("완충재")
    private CushioningMaterial cushioningMaterial;
    @Column(name = "cushioning_material_quantity", nullable = false)
    @Comment("완충재 수량")
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
            final Long recommendedPackagingMaterialId,
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
        this.recommendedPackagingMaterialId = recommendedPackagingMaterialId;
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
        Assert.notNull(priorityDelivery, "우선 배송 여부는 필수입니다.");
        Assert.notNull(desiredDeliveryDate, "희망 배송일은 필수입니다.");
        Assert.notNull(orderedAt, "주문 일시는 필수입니다.");
    }

    public void addOutboundItem(final OutboundItem outboundItem) {
        outboundItems.add(outboundItem);
        outboundItem.assignOutbound(this);
    }

    public Outbound split(final List<OutboundItemToSplit> outboundItemToSplits) {
        validateSplit(outboundItemToSplits);
        return null;
    }

    /**
     * 출고를 분할할 수 있는지 검증한다.
     * 출고를 분할하기 위해서는 출고는 반드시 대기 상태여야 한다.
     * 분할한 뒤 기존 출고의 상품이 하나도 남아있지 않으면 안된다.
     */
    private void validateSplit(final List<OutboundItemToSplit> outboundItemToSplits) {
        if (outboundStatus != OutboundStatus.READY) {
            throw new IllegalStateException("출고는 대기 상태에서만 분할할 수 있습니다.");
        }
        Assert.notEmpty(outboundItemToSplits, "분할할 상품은 1개 이상이어야 합니다.");
        if (outboundItemToSplits.size() > outboundItems.size()) {
            throw new IllegalArgumentException("분할할 상품은 출고 상품의 개수보다 많을 수 없습니다.");
        }
        final int totalQuantityOfSplit = outboundItemToSplits.stream()
                .mapToInt(OutboundItemToSplit::getQuantityOfSplit)
                .sum();
        final int totalQuantityOfItem = outboundItems.stream()
                .mapToInt(OutboundItem::getOutboundQuantity)
                .sum();
        if (totalQuantityOfSplit >= totalQuantityOfItem) {
            throw new IllegalArgumentException("분할할 상품의 총 수량은 출고 상품의 총 수량보다 작아야 합니다.");
        }
    }
}
