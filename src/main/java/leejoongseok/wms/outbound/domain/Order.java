package leejoongseok.wms.outbound.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Order {
    private Long id;
    private String customerAddress;
    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerZipCode;
    private LocalDate desiredDeliveryDate;
    private boolean isPriorityDelivery;
    private String outboundRequirements;
    private String deliveryRequirements;
    private LocalDateTime orderedAt;

    public List<OrderItem> getOrderItems() {
        return null;
    }

}
