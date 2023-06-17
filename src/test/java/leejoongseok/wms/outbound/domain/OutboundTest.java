package leejoongseok.wms.outbound.domain;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class OutboundTest {

    @Test
    @DisplayName("출고를 분할 한다.")
    void split() {
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final Long outboundId = 1L;
        final Long outboundItemId = 1L;
        final Integer outboundItemQuantity = 2;
        final OutboundItem outboundItem = createOutboundItem(
                outboundItemId,
                outboundItemQuantity);
        final Outbound outbound = createOutbound(
                readyStatus,
                outboundId);
        outbound.addOutboundItem(outboundItem);

        final Integer cushioningMaterialQuantity = 1;
        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 1;
        final OutboundItemToSplit outboundItemToSplit = new OutboundItemToSplit(
                outboundItemIdToSplit,
                quantityOfSplit);

        final Outbound splittedOutbound = outbound.split(
                cushioningMaterialQuantity,
                List.of(outboundItemToSplit));
    }

    private Outbound createOutbound(
            final OutboundStatus status,
            final Long outboundId) {
        return Instancio.of(Outbound.class)
                .supply(Select.field(Outbound::getOutboundStatus), () -> status)
                .supply(Select.field(Outbound::getId), () -> outboundId)
                .ignore(Select.field(Outbound::getOutboundItems))
                .create();
    }

    private OutboundItem createOutboundItem(
            final Long outboundItemId,
            final Integer outboundItemQuantity) {
        return Instancio.of(OutboundItem.class)
                .supply(Select.field(OutboundItem::getId), () -> outboundItemId)
                .supply(Select.field(OutboundItem::getOutboundQuantity), () -> outboundItemQuantity)
                .create();
    }
}