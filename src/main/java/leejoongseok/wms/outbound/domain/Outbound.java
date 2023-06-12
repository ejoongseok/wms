package leejoongseok.wms.outbound.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Outbound {
    List<OutboundItem> outboundItems = new ArrayList<>();

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
            final boolean priorityDelivery,
            final LocalDate desiredDeliveryDate,
            final String outboundRequirements,
            final String deliveryRequirements,
            final LocalDateTime orderedAt) {
        //TODO Assert 적용해야함.

        throw new UnsupportedOperationException("Unsupported Outbound");
    }

    public void addOutboundItem(final OutboundItem outboundItem) {
        outboundItems.add(outboundItem);
        outboundItem.assignOutbound(this);
    }
}
