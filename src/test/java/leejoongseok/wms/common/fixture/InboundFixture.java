package leejoongseok.wms.common.fixture;

import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundStatus;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InboundFixture {

    private static final LocalDateTime orderRequestAt = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime estimatedArrivalAt = LocalDateTime.now().plusDays(1);
    private InstancioApi<Inbound> inboundInstance = Instancio.of(Inbound.class)
            .ignore(Select.field(Inbound::getInboundItems))
            .supply(Select.field(Inbound::getStatus), () -> InboundStatus.ORDER_REQUESTED);

    public static InboundFixture aInbound() {
        return new InboundFixture();
    }

    public static InboundFixture aDefaultInbound() {
        return aInbound()
                .withOrderRequestAt(orderRequestAt)
                .withEstimatedArrivalAt(estimatedArrivalAt);
    }

    public InboundFixture withOrderRequestAt(final LocalDateTime orderRequestAt) {
        inboundInstance.supply(Select.field(Inbound::getOrderRequestAt), () -> orderRequestAt);
        return this;
    }

    public InboundFixture withEstimatedArrivalAt(final LocalDateTime estimatedArrivalAt) {
        inboundInstance.supply(Select.field(Inbound::getEstimatedArrivalAt), () -> estimatedArrivalAt);
        return this;
    }

    public InboundFixture withTotalAmount(final BigDecimal totalAmount) {
        inboundInstance.supply(Select.field(Inbound::getTotalAmount), () -> totalAmount);
        return this;
    }

    public Inbound build() {
        inboundInstance = null == inboundInstance ? Instancio.of(Inbound.class) : inboundInstance;
        return inboundInstance.create();
    }
}
