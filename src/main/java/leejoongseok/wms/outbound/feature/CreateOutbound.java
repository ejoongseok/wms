package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.CushioningMaterial;
import leejoongseok.wms.outbound.domain.Order;
import leejoongseok.wms.outbound.domain.OrderItem;
import leejoongseok.wms.outbound.port.LoadOrderPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CreateOutbound {
    private final LoadOrderPort loadOrderPort;
    private final LocationLPNRepository locationLPNRepository;

    public void request(final Request request) {
        final Order order = loadOrderPort.getBy(request.orderId);
        final List<OrderItem> orderItems = order.getOrderItems();
        for (final OrderItem orderItem : orderItems) {
            // 주문 상품에 맞는 로케이션 LPN을 조회한다.
            final List<LocationLPN> locationLPNList = locationLPNRepository.findByItemIdAndFetchJoinLPN(orderItem.getItemId());
            validateAvailableInventoryQuantity(locationLPNList, orderItem.getOrderQuantity());
            //
        }

        for (final OrderItem orderItem : orderItems) {
            orderItem.getItemId();
        }
    }

    private void validateAvailableInventoryQuantity(final List<LocationLPN> locationLPNList, final Integer orderQuantity) {
        // 유통기한이 지난 로케이션 LPN을 제외한다.
        final List<LocationLPN> nonExpiredLocationLPNList = filterByNonExpiredLocationLPN(locationLPNList);
        // 유통기한이 지난 로케이션 LPN을 제외한 재고 수량을 계산한다.
        final int availableInventoryQuantity = calculateAvailableInventoryQuantity(nonExpiredLocationLPNList);
        // 출고 요청 수량이 출고 가능한 재고 수량보다 크면 예외를 발생시킨다.
        if (availableInventoryQuantity < orderQuantity) {
            throw new IllegalArgumentException(
                    "출고 가능한 재고가 부족합니다. " +
                            "가용 가능한 재고 수량: " + availableInventoryQuantity +
                            " 출고 요청 수량: " + orderQuantity);
        }
    }

    private List<LocationLPN> filterByNonExpiredLocationLPN(final List<LocationLPN> locationLPNList) {
        return locationLPNList.stream()
                .filter(locationLPN -> locationLPN.getLpn().getExpirationAt().isAfter(LocalDateTime.now()))
                .toList();
    }

    private int calculateAvailableInventoryQuantity(final List<LocationLPN> nonExpiredLocationLPNList) {
        final int availableInventoryQuantity = nonExpiredLocationLPNList.stream()
                .mapToInt(LocationLPN::getInventoryQuantity)
                .sum();
        return availableInventoryQuantity;
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
