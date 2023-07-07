package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.item.domain.Item;
import leejoongseok.wms.item.domain.ItemRepository;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.CushioningMaterial;
import leejoongseok.wms.outbound.domain.LocationLPNFilterForOutbound;
import leejoongseok.wms.outbound.domain.LocationLPNValidatorForOutbound;
import leejoongseok.wms.outbound.domain.OrderPackagingMaterialRecommender;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundCustomer;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.domain.PackagingMaterial;
import leejoongseok.wms.outbound.domain.PackagingMaterialRecommender;
import leejoongseok.wms.outbound.domain.PackagingMaterialRepository;
import leejoongseok.wms.outbound.exception.ItemIdNotFoundException;
import leejoongseok.wms.outbound.port.LoadOrderPort;
import leejoongseok.wms.outbound.port.Order;
import leejoongseok.wms.outbound.port.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 주문에 대한 출고를 생성하는 컨트롤러 클래스.
 */
@RestController
@RequiredArgsConstructor
public class CreateOutbound {
    private final LoadOrderPort loadOrderPort;
    private final LocationLPNRepository locationLPNRepository;
    private final PackagingMaterialRepository packagingMaterialRepository;
    private final OutboundRepository outboundRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @PostMapping("/outbounds")
    @ResponseStatus(HttpStatus.CREATED)
    public void request(@RequestBody @Valid final Request request) {
        final Order order = loadOrderPort.getBy(request.orderId);
        final List<OrderItem> orderItems = order.getOrderItems();
        validateForOutboundCreation(orderItems);

        final Optional<PackagingMaterial> packagingMaterial =
                findPackagingMaterialForOutbound(
                        orderItems,
                        request.cushioningMaterial,
                        request.cushioningMaterialQuantity);

        final Outbound outbound = createOutbound(
                order,
                packagingMaterial,
                request.cushioningMaterial,
                request.cushioningMaterialQuantity);

        outboundRepository.save(outbound);
    }

    private void validateForOutboundCreation(
            final List<OrderItem> orderItems) {
        for (final OrderItem orderItem : orderItems) {
            // 주문 상품에 맞는 로케이션 LPN을 조회한다.
            final List<LocationLPN> locationLPNList =
                    locationLPNRepository.findByItemIdAndFetchJoinLPN(orderItem.getItemId());

            // 출고 가능한 로케이션 LPN으로 필터링한다.
            final LocalDateTime thisDateTime = LocalDateTime.now();
            final List<LocationLPN> filteredLocationLPNList =
                    LocationLPNFilterForOutbound.filter(locationLPNList, thisDateTime);

            // 출고 가능한 로케이션 LPN이 충분한지 검증한다.
            LocationLPNValidatorForOutbound.validate(filteredLocationLPNList, orderItem.getOrderQuantity());
        }
    }

    private Optional<PackagingMaterial> findPackagingMaterialForOutbound(
            final List<OrderItem> orderItems,
            final CushioningMaterial cushioningMaterial,
            final Integer cushioningMaterialQuantity) {
        final PackagingMaterialRecommender packagingMaterialRecommender =
                new OrderPackagingMaterialRecommender(
                        packagingMaterialRepository.findAll(),
                        orderItems,
                        cushioningMaterial.calculateTotalVolume(cushioningMaterialQuantity),
                        cushioningMaterial.calculateTotalWeightInGrams(cushioningMaterialQuantity));
        return packagingMaterialRecommender.recommendPackagingMaterial();
    }

    private Outbound createOutbound(
            final Order order,
            final Optional<PackagingMaterial> packagingMaterial,
            final CushioningMaterial cushioningMaterial,
            final Integer cushioningMaterialQuantity) {
        final PackagingMaterial recommendedPackagingMaterial = packagingMaterial.orElse(null);
        final Outbound outbound = newOutbound(
                order,
                cushioningMaterial,
                cushioningMaterialQuantity,
                recommendedPackagingMaterial);
        final List<OutboundItem> outboundItems = newOutboundItems(order.getOrderItems());
        outboundItems.forEach(outbound::addOutboundItem);
        return outbound;
    }

    private Outbound newOutbound(
            final Order order,
            final CushioningMaterial cushioningMaterial,
            final Integer cushioningMaterialQuantity,
            final PackagingMaterial recommendedPackagingMaterial) {
        return new Outbound(
                order.getId(),
                recommendedPackagingMaterial,
                cushioningMaterial,
                cushioningMaterialQuantity,
                order.getIsPriorityDelivery(),
                order.getDesiredDeliveryDate(),
                order.getOutboundRequirements(),
                order.getDeliveryRequirements(),
                order.getOrderedAt(), new OutboundCustomer(
                order.getCustomerAddress(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerPhoneNumber(),
                order.getCustomerZipCode())
        );
    }

    private List<OutboundItem> newOutboundItems(
            final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OutboundItem(
                        getItem(orderItem.getItemId()),
                        orderItem.getOrderQuantity(),
                        orderItem.getUnitPrice()))
                .toList();
    }

    private Item getItem(final Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemIdNotFoundException(itemId));
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
            LocalDate desiredDeliveryDate) {

    }
}
