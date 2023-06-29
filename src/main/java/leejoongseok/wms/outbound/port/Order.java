package leejoongseok.wms.outbound.port;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 오픈마켓에서 주문한 주문 정보
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    private Long id;
    private String customerAddress;
    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerZipCode;
    private LocalDate desiredDeliveryDate;
    private Boolean isPriorityDelivery;
    private String outboundRequirements;
    private String deliveryRequirements;
    private LocalDateTime orderedAt;
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(
            final Long id,
            final String customerAddress,
            final String customerName,
            final String customerEmail,
            final String customerPhoneNumber,
            final String customerZipCode,
            final LocalDate desiredDeliveryDate,
            final Boolean isPriorityDelivery,
            final String outboundRequirements,
            final String deliveryRequirements,
            final LocalDateTime orderedAt,
            final List<OrderItem> orderItems) {
        validateConstructor(
                customerAddress,
                customerName,
                customerEmail,
                customerPhoneNumber,
                customerZipCode,
                desiredDeliveryDate,
                isPriorityDelivery,
                orderedAt,
                orderItems);
        this.id = id;
        this.customerAddress = customerAddress;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerZipCode = customerZipCode;
        this.desiredDeliveryDate = desiredDeliveryDate;
        this.isPriorityDelivery = isPriorityDelivery;
        this.outboundRequirements = outboundRequirements;
        this.deliveryRequirements = deliveryRequirements;
        this.orderedAt = orderedAt;
        this.orderItems = orderItems;
    }

    private void validateConstructor(
            final String customerAddress,
            final String customerName,
            final String customerEmail,
            final String customerPhoneNumber,
            final String customerZipCode,
            final LocalDate desiredDeliveryDate,
            final Boolean isPriorityDelivery,
            final LocalDateTime orderedAt,
            final List<OrderItem> orderItems) {
        Assert.hasText(customerAddress, "주문자 주소는 필수입니다.");
        Assert.hasText(customerName, "주문자 이름은 필수입니다.");
        Assert.hasText(customerEmail, "주문자 이메일은 필수입니다.");
        Assert.hasText(customerPhoneNumber, "주문자 전화번호는 필수입니다.");
        Assert.hasText(customerZipCode, "주문자 우편번호는 필수입니다.");
        Assert.notNull(orderedAt, "주문일은 필수입니다.");
        Assert.notEmpty(orderItems, "주문 상품은 필수입니다.");
        Assert.notNull(desiredDeliveryDate, "희망 배송일은 필수입니다.");
        Assert.notNull(isPriorityDelivery, "우선 배송 여부는 필수입니다.");
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

}
