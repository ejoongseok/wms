package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.CushioningMaterial;
import leejoongseok.wms.outbound.domain.LocationLPNFilterForOutbound;
import leejoongseok.wms.outbound.domain.LocationLPNValidatorForOutbound;
import leejoongseok.wms.outbound.domain.Order;
import leejoongseok.wms.outbound.domain.OrderItem;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.PackagingMaterialSelectorForOutbound;
import leejoongseok.wms.outbound.port.LoadOrderPort;
import leejoongseok.wms.packaging.domain.PackagingMaterial;
import leejoongseok.wms.packaging.domain.PackagingMaterialRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CreateOutbound {
    private final LoadOrderPort loadOrderPort;
    private final LocationLPNRepository locationLPNRepository;
    private final LocationLPNFilterForOutbound locationLPNFilterForOutbound;
    private final LocationLPNValidatorForOutbound locationLPNValidatorForOutbound;
    private final PackagingMaterialRepository packagingMaterialRepository;

    public void request(final Request request) {
        final Order order = loadOrderPort.getBy(request.orderId);
        final List<OrderItem> orderItems = order.getOrderItems();
        validateForOutboundCreation(orderItems);

        final Optional<PackagingMaterial> packagingMaterial = findPackagingMaterialForOutbound(
                request,
                orderItems);

        final Outbound outbound = createOutbound(
                order,
                packagingMaterial,
                request.cushioningMaterial,
                request.cushioningMaterialQuantity);
    }

    private Outbound createOutbound(
            final Order order,
            final Optional<PackagingMaterial> packagingMaterial,
            final CushioningMaterial cushioningMaterial,
            final Integer cushioningMaterialQuantity) {
        final Long recommendedPackagingMaterialId = packagingMaterial
                .map(PackagingMaterial::getId)
                .orElse(null);
        final Outbound outbound = new Outbound(
                order.getId(),
                recommendedPackagingMaterialId,
                order.getCustomerAddress(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerPhoneNumber(),
                order.getCustomerZipCode(),
                order.getCustomerMessage(),
                cushioningMaterial,
                cushioningMaterialQuantity,
                order.isPriorityDelivery(),
                order.getDesiredDeliveryDate(),
                order.getOutboundRequirements(),
                order.getDeliveryRequirements(),
                order.getOrderedAt()
        );
        for (final OrderItem orderItem : order.getOrderItems()) {
            final OutboundItem outboundItem = new OutboundItem(
                    orderItem.getItemId(),
                    orderItem.getOrderQuantity(),
                    orderItem.getUnitPrice());
            outbound.addOutboundItem(outboundItem);
        }
        return outbound;
    }

    private Optional<PackagingMaterial> findPackagingMaterialForOutbound(
            final Request request,
            final List<OrderItem> orderItems) {
        final int cushioningMaterialVolume =
                request.cushioningMaterial.getVolume() * request.cushioningMaterialQuantity;
        final int cushioningMaterialWeightInGrams =
                request.cushioningMaterial.getWeightInGrams() * request.cushioningMaterialQuantity;
        final PackagingMaterialSelectorForOutbound packagingMaterialSelectorForOutbound =
                new PackagingMaterialSelectorForOutbound(packagingMaterialRepository.findAll());
        return packagingMaterialSelectorForOutbound.select(
                orderItems,
                cushioningMaterialVolume,
                cushioningMaterialWeightInGrams);
    }

    private void validateForOutboundCreation(final List<OrderItem> orderItems) {
        for (final OrderItem orderItem : orderItems) {
            // 주문 상품에 맞는 로케이션 LPN을 조회한다.
            final List<LocationLPN> locationLPNList = locationLPNRepository.findByItemIdAndFetchJoinLPN(
                    orderItem.getItemId());
            // 출고 가능한 로케이션 LPN으로 필터링한다.
            final LocalDateTime expirationAtToFilter = LocalDateTime.now();
            final List<LocationLPN> filteredLocationLPNList = locationLPNFilterForOutbound.filter(
                    locationLPNList,
                    expirationAtToFilter);
            // 출고 가능한 로케이션 LPN이 충분한지 검증한다.
            locationLPNValidatorForOutbound.validate(
                    filteredLocationLPNList,
                    orderItem.getOrderQuantity());
        }
    }

    public record Request(
            @NotNull(message = "주문번호는 필수입니다.")
            Long orderId,
            @NotNull(message = "완충재는 필수 입니다.")
            CushioningMaterial cushioningMaterial,
            @NotNull(message = "완충재 수량은 필수 입니다.")
            @Min(value = 0, message = "완충재 수량은 0보다 커야 합니다.")
            Integer cushioningMaterialQuantity,
            @NotNull(message = "우선배송 여부는 필수 입니다.")
            Boolean isPriorityDelivery,
            @NotNull(message = "희망 배송일은 필수 입니다.")
            @FutureOrPresent(message = "희망 배송일은 과거일 수 없습니다.")
            LocalDate desiredDeliveryDate) {

    }
}
