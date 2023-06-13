package leejoongseok.wms.outbound.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

}
