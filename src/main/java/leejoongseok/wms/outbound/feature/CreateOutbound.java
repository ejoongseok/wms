package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.CushioningMaterial;
import leejoongseok.wms.outbound.domain.Order;
import leejoongseok.wms.outbound.domain.OrderItem;
import leejoongseok.wms.outbound.domain.OutboundLocationLPNList;
import leejoongseok.wms.outbound.port.LoadOrderPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class CreateOutbound {
    private final LoadOrderPort loadOrderPort;
    private final LocationLPNRepository locationLPNRepository;
    private final OutboundLocationLPNList outboundLocationLPNList = new OutboundLocationLPNList();

    public void request(final Request request) {
        final Order order = loadOrderPort.getBy(request.orderId);
        final List<OrderItem> orderItems = order.getOrderItems();
        for (final OrderItem orderItem : orderItems) {
            // 주문 상품에 맞는 로케이션 LPN을 조회한다.
            final List<LocationLPN> locationLPNList = locationLPNRepository.findByItemIdAndFetchJoinLPN(orderItem.getItemId());
            outboundLocationLPNList.validateAvailableInventoryQuantity(locationLPNList, orderItem.getOrderQuantity());
            //
        }

        for (final OrderItem orderItem : orderItems) {
            orderItem.getItemId();
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
