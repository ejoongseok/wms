package leejoongseok.wms.outbound.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outbound")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("출고")
public class Outbound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("출고 ID")
    private Long id;
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
    @OneToMany(mappedBy = "outbound", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<OutboundItem> outboundItems = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "outbound_status", nullable = false)
    @Comment("출고 상태")
    private final OutboundStatus outboundStatus = OutboundStatus.READY;

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
}
