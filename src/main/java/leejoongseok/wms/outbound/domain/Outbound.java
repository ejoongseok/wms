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
import leejoongseok.wms.location.domain.Location;
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
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "outbound_status", nullable = false)
    @Comment("출고 상태")
    private OutboundStatus outboundStatus = OutboundStatus.READY;
    @Column(name = "order_id", nullable = false)
    @Comment("주문 ID")
    private Long orderId;
    @Getter
    @OneToMany(mappedBy = "outbound", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OutboundItem> outboundItems = new ArrayList<>();
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("출고 ID")
    private Long id;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_packaging_material_id", nullable = true)
    @Comment("추천 포장재 ID")
    private PackagingMaterial recommendedPackagingMaterial;
    @Embedded
    private OutboundCustomer outboundCustomer;
    @Getter(AccessLevel.PROTECTED)
    @Enumerated(EnumType.STRING)
    @Column(name = "cushioning_material", nullable = false)
    @Comment("완충재")
    private CushioningMaterial cushioningMaterial;
    @Getter(AccessLevel.PROTECTED)
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
    @Getter
    @JoinColumn(name = "tote_location_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("토트 로케이션 ID")
    private Location toteLocation;
    @Getter
    @Column(name = "tracking_number", nullable = true)
    @Comment("송장 번호")
    private String trackingNumber;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_packaging_material_id", nullable = true)
    @Comment("실제 포장한 포장재 ID")
    private PackagingMaterial realPackagingMaterial;
    @Column(name = "real_packaging_weight_in_grams", nullable = true)
    @Comment("실제 포장 중량")
    private Integer realPackagingWeightInGrams;

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
            final List<SplittableOutboundItem> splittableOutboundItems) {
        validateSplit(splittableOutboundItems);
        final List<OutboundItem> splittedOutboundItems = splitOutboundItems(
                splittableOutboundItems);
        final Outbound cloneNewOutbound = cloneNewOutbound(splittedOutboundItems);
        decreaseOutboundItemQuantityBySplit(splittableOutboundItems);
        clearEmptyOutboundItemsAfterSplit();
        return cloneNewOutbound;
    }

    /**
     * 출고를 분할 할 수 있는지 검증한다.
     * 출고를 분할하기 위해서는 출고는 반드시 대기 상태여야 한다.
     * 분할한 뒤 기존 출고의 상품이 하나도 남아있지 않으면 안된다.
     */
    private void validateSplit(
            final List<SplittableOutboundItem> splittableOutboundItems) {
        if (OutboundStatus.READY != outboundStatus) {
            throw new IllegalStateException(
                    "출고는 대기 상태에서만 분할 할 수 있습니다.");
        }
        Assert.notEmpty(splittableOutboundItems,
                "분할 대상 출고 상품은 1개 이상이어야 합니다.");
        if (splittableOutboundItems.size() > outboundItems.size()) {
            throw new IllegalArgumentException(
                    "분할 대상 출고 상품은 출고 상품의 개수보다 많을 수 없습니다.");
        }
        final int totalQuantityOfSplit = splittableOutboundItems.stream()
                .mapToInt(SplittableOutboundItem::getQuantityToSplit)
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
            final List<SplittableOutboundItem> splittableOutboundItems) {
        return splittableOutboundItems.stream()
                .map(splittableOutboundItem ->
                        getOutboundItem(splittableOutboundItem.getOutboundItemId())
                                .split(splittableOutboundItem.getQuantityToSplit()))
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
     * 분할한 만큼 현재 출고 상품의 수량을 감소시킨다.
     */
    private void decreaseOutboundItemQuantityBySplit(
            final List<SplittableOutboundItem> splittableOutboundItems) {
        for (final SplittableOutboundItem splittableOutboundItem : splittableOutboundItems) {
            final OutboundItem outboundItem = getOutboundItem(splittableOutboundItem.getOutboundItemId());
            outboundItem.decreaseQuantity(splittableOutboundItem.getQuantityToSplit());
        }
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
        Assert.notNull(packagingMaterial, "추천 포장재는 null이 될 수 없습니다.");
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

    public boolean isReadyStatus() {
        return OutboundStatus.READY == outboundStatus;
    }

    public boolean hasAssignedTote() {
        return null != toteLocation;
    }

    public void assignPickingTote(final Location tote) {
        validateAssignPickingTote(tote);
        toteLocation = tote;
    }

    private void validateAssignPickingTote(final Location tote) {
        final String locationBarcode = tote.getLocationBarcode();
        if (!tote.isTote()) {
            throw new IllegalArgumentException(
                    "로케이션 바코드[%s]는 토트가 아닙니다.".formatted(locationBarcode));
        }
        if (tote.hasLocationLPN()) {
            throw new IllegalArgumentException(
                    "집품에 사용할 토트에 상품이 이미 담겨 있습니다. 로케이션바코드[%s]"
                            .formatted(locationBarcode));
        }
        if (!isReadyStatus()) {
            throw new IllegalArgumentException(
                    ("집품할 토트 할당은 출고 대기상태에만 가능합니다. " +
                            "출고 상태: %s").formatted(
                            outboundStatus.getDescription()));
        }
        if (hasAssignedTote()) {
            throw new IllegalStateException("이미 할당된 토트가 존재합니다.");
        }
        if (null == recommendedPackagingMaterial) {
            throw new IllegalStateException("추천 포장재가 할당되지 않았습니다.");
        }
    }

    public boolean isPickingInProgress() {
        return OutboundStatus.PICKING_IN_PROGRESS == outboundStatus;
    }

    public boolean isPickingReadyStatus() {
        return hasAssignedTote() && OutboundStatus.PICKING_READY == outboundStatus;
    }

    public void startPickingReady() {
        validateStartPickingReady();
        outboundStatus = OutboundStatus.PICKING_READY;
    }

    private void validateStartPickingReady() {
        if (!isReadyStatus()) {
            throw new IllegalStateException(
                    "집품대기 상태가 되기 위해서는 출고 준비 상태여야 합니다. 현재 상태: %s".formatted(
                            outboundStatus.getDescription()));
        }
        if (!hasAssignedTote()) {
            throw new IllegalStateException("집품 대기 상태가 되기 위해서는 할당된 토트가 필요합니다.");
        }
    }

    public void startPickingProgress() {
        validateStartPicking();
        outboundStatus = OutboundStatus.PICKING_IN_PROGRESS;
    }

    private void validateStartPicking() {
        if (!isPickingReadyStatus()) {
            throw new IllegalStateException(
                    "집품 진행 상태가 되기 위해서는 집품 대기 상태여야 합니다. 현재 상태: %s".formatted(
                            outboundStatus.getDescription()));
        }
    }

    /**
     * Picking에 할당된 집품 수량만큼 LocationLPN의 재고 수량을 감소시킨다.
     */
    public void deductAllocatedInventory() {
        validateDeductAllocatedInventory();
        for (final OutboundItem outboundItem : outboundItems) {
            outboundItem.deductAllocatedInventory();
        }
    }

    private void validateDeductAllocatedInventory() {
        final List<Picking> pickings = outboundItems.stream()
                .map(outboundItem -> outboundItem.getPickings())
                .flatMap(List::stream)
                .toList();
        final boolean isToteContainsItemPicked = pickings.stream()
                .anyMatch(picking -> picking.hasPickedItem());
        if (!isPickingReadyStatus() || isToteContainsItemPicked) {
            throw new IllegalStateException(
                    ("Picking에 할당된 집품 수량만큼 LocationLPN의 재고 수량을 감소시키기 위해서는 " +
                            "집품 대기 상태여야 하고 토트에 집품한 상품이 없어야합니다. 현재 상태: %s").formatted(
                            outboundStatus.getDescription()));
        }
    }

    public List<Long> getItemIds() {
        return outboundItems.stream()
                .map(OutboundItem::getItemId)
                .toList();
    }

    /**
     * 집품 완료 처리
     */
    public void completePicking() {
        validateCompletePicking();
        outboundStatus = OutboundStatus.PICKING_COMPLETED;
    }

    private void validateCompletePicking() {
        if (!isPickingInProgress()) {
            throw new IllegalStateException(
                    "집품 완료 처리를 위해서는 집품 진행 상태여야 합니다. 현재 상태: %s".formatted(
                            outboundStatus.getDescription()));
        }
        final boolean isAllCompletedPicking = outboundItems.stream()
                .allMatch(OutboundItem::isCompletedPicking);
        if (!isAllCompletedPicking) {
            throw new IllegalStateException(
                    "집품 완료 처리를 위해서는 모든 상품의 집품이 완료되어야 합니다.");
        }
    }

    public boolean isCompletedPicking() {
        return OutboundStatus.PICKING_COMPLETED == outboundStatus;
    }

    public boolean hasTrackingNumber() {
        return null != trackingNumber;
    }

    public void assignTrackingNumber(final String trackingNumber) {
        validateAssignTrackingNumber(trackingNumber);
        this.trackingNumber = trackingNumber;
    }

    private void validateAssignTrackingNumber(final String trackingNumber) {
        Assert.hasText(trackingNumber, "송장번호는 필수입니다.");
        if (hasTrackingNumber()) {
            throw new IllegalStateException("이미 할당된 송장번호가 존재합니다.");
        }
    }

    /**
     * 실제로 포장한 포장재와 실중량을 할당한다.
     */
    public void assignPacking(
            final PackagingMaterial packagingMaterial,
            final Integer packagingWeightInGrams) {
        validateAssignPacking(packagingMaterial, packagingWeightInGrams);
        realPackagingMaterial = packagingMaterial;
        realPackagingWeightInGrams = packagingWeightInGrams;
        outboundStatus = OutboundStatus.PACKING_IN_PROGRESS;
    }

    private void validateAssignPacking(
            final PackagingMaterial packagingMaterial,
            final Integer realWeightInGrams) {
        Assert.notNull(packagingMaterial, "포장재는 필수입니다.");
        Assert.notNull(realWeightInGrams, "실중량은 필수입니다.");
        if (0 > realWeightInGrams) {
            throw new IllegalArgumentException("실중량은 0보다 작을 수 없습니다.");
        }
        if (!isCompletedPicking()) {
            throw new IllegalStateException(
                    "포장을 위해서는 집품이 완료되어야 합니다.");
        }
        final Long totalWeightInGrams = calculateTotalWeightInGrams();
        final Long diff = Math.abs(totalWeightInGrams - realWeightInGrams);
        final Integer weightErrorTolerance = 100;
        if (weightErrorTolerance < diff) {
            throw new IllegalArgumentException(
                    "실중량과 상품의 포장예상 총중량의 차이가 100g 이상입니다. " +
                            "실중량: %d, 상품의 포장예상 총중량: %d".formatted(
                                    realWeightInGrams, totalWeightInGrams));
        }
    }

    public boolean isPackingInProgress() {
        return OutboundStatus.PACKING_IN_PROGRESS == outboundStatus;
    }

    public void completePacking() {
        validateCompletePacking();
        outboundStatus = OutboundStatus.PACKING_COMPLETED;
    }

    private void validateCompletePacking() {
        Assert.hasText(trackingNumber, "포장을 완료하기 위해서는 송장이 발행되어야합니다.");
        if (!isPackingInProgress()) {
            throw new IllegalStateException(
                    "포장 완료 처리를 위해서는 포장 진행 상태여야 합니다. 현재 상태: %s".formatted(
                            outboundStatus.getDescription()));
        }
    }

    public boolean isCompletedPacking() {
        return OutboundStatus.PACKING_COMPLETED == outboundStatus;
    }

    public void passInspection() {
        validatePassInspection();
        outboundStatus = OutboundStatus.INSPECTION_PASSED;
    }

    private void validatePassInspection() {
        if (!isCompletedPicking()) {
            throw new IllegalStateException(
                    "검수 통과 처리를 위해서는 집품이 완료되어야 합니다.");
        }
    }

    public boolean isPassedInspection() {
        return OutboundStatus.INSPECTION_PASSED == outboundStatus;
    }
}
