package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.outbound.exception.OutboundItemIdNotFoundException;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboundTest {

    @Test
    @DisplayName("출고를 분할 한다.")
    void split() {
        final long outboundId = 1L;
        final long outboundItemId = 1L;
        final int outboundItemQuantity = 2;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final Outbound outbound = createSplitTargetOutbound(
                outboundId,
                outboundItemId,
                outboundItemQuantity,
                readyStatus);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 1;
        final OutboundItemToSplit outboundItemToSplit = createOutboundItemToSplit(
                outboundItemIdToSplit,
                quantityOfSplit);

        final Outbound splittedOutbound = outbound.split(
                List.of(outboundItemToSplit));

        assertThat(splittedOutbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(splittedOutbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
    }

    private Outbound createSplitTargetOutbound(
            final Long outboundId,
            final Long outboundItemId,
            final Integer outboundItemQuantity,
            final OutboundStatus outboundStatus) {
        final OutboundItem outboundItem = createOutboundItem(
                outboundItemId,
                outboundItemQuantity);
        final Outbound outbound = createOutbound(
                outboundStatus,
                outboundId);
        outbound.addOutboundItem(outboundItem);
        return outbound;
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

    private OutboundItemToSplit createOutboundItemToSplit(
            final Long outboundItemIdToSplit,
            final Integer quantityOfSplit) {
        return new OutboundItemToSplit(
                outboundItemIdToSplit,
                quantityOfSplit);
    }

    @Test
    @DisplayName("출고를 분할 한다. [출고 상태가 READY가 아닌 경우]")
    void fail_split_invalid_outbound_status() {
        final long outboundId = 1L;
        final long outboundItemId = 1L;
        final int outboundItemQuantity = 2;
        final OutboundStatus invalidStatus = OutboundStatus.PICKING;
        final Outbound outbound = createSplitTargetOutbound(
                outboundId,
                outboundItemId,
                outboundItemQuantity,
                invalidStatus);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 1;
        final OutboundItemToSplit outboundItemToSplit = createOutboundItemToSplit(
                outboundItemIdToSplit,
                quantityOfSplit);

        assertThatThrownBy(() -> {
            outbound.split(List.of(outboundItemToSplit));
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("출고는 대기 상태에서만 분할할 수 있습니다.");
    }

    @Test
    @DisplayName("출고를 분할 한다. [분할 하려는 출고 품목이 존재하지 않는 경우]")
    void fail_split_not_found_target_outbound_item_id() {
        final long outboundId = 1L;
        final long outboundItemId = 1L;
        final int outboundItemQuantity = 2;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final Outbound outbound = createSplitTargetOutbound(
                outboundId,
                outboundItemId,
                outboundItemQuantity,
                readyStatus);

        final Long outboundItemIdToSplit = 2L;
        final Integer quantityOfSplit = 1;
        final OutboundItemToSplit outboundItemToSplit = createOutboundItemToSplit(
                outboundItemIdToSplit,
                quantityOfSplit);

        assertThatThrownBy(() -> {
            outbound.split(List.of(outboundItemToSplit));
        }).isInstanceOf(OutboundItemIdNotFoundException.class)
                .hasMessageContaining("출고 상품 ID [2]에 해당하는 출고 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("출고를 분할 한다. [분할 하려는 출고 상품의 수량이 출고 상품의 수량보다 많은 경우]")
    void fail_split_over_split_quantity_target_outbound_item_quantity() {
        final long outboundId = 1L;
        final long outboundItemId = 1L;
        final int outboundItemQuantity = 2;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final Outbound outbound = createSplitTargetOutbound(
                outboundId,
                outboundItemId,
                outboundItemQuantity,
                readyStatus);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 2;
        final OutboundItemToSplit outboundItemToSplit = createOutboundItemToSplit(
                outboundItemIdToSplit,
                quantityOfSplit);

        assertThatThrownBy(() -> {
            outbound.split(List.of(outboundItemToSplit));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("분할하려는 상품의 총 수량은 출고 상품의 총 수량보다 작아야 합니다. 분할하려는 상품의 총 수량: 2, 출고 상품의 총 수량: 2");
    }

    @Test
    @DisplayName("출고를 분할 한다. [출고 상품 두개 중 하나만 아예 분할하는 경우]")
    void split_1() {
        final long outboundId = 1L;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final OutboundItem outboundItem = createOutboundItem(
                1L,
                2);
        final OutboundItem outboundItem2 = createOutboundItem(
                2L,
                2);
        final Outbound outbound = createOutbound(
                readyStatus,
                outboundId);
        outbound.addOutboundItem(outboundItem);
        outbound.addOutboundItem(outboundItem2);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 2;
        final OutboundItemToSplit outboundItemToSplit = createOutboundItemToSplit(
                outboundItemIdToSplit,
                quantityOfSplit);

        final Outbound splittedOutbound = outbound.split(
                List.of(outboundItemToSplit));

        assertThat(splittedOutbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(splittedOutbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(2);
        assertThat(outbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(2);
        assertThat(outbound.getOutboundItems().get(0).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("출고를 분할 한다. [출고 상품 두개 중 하나의 일부 수량만 분할하는 경우]")
    void split_2() {
        final long outboundId = 1L;
        final OutboundStatus readyStatus = OutboundStatus.READY;
        final OutboundItem outboundItem = createOutboundItem(
                1L,
                2);
        final OutboundItem outboundItem2 = createOutboundItem(
                2L,
                2);
        final Outbound outbound = createOutbound(
                readyStatus,
                outboundId);
        outbound.addOutboundItem(outboundItem);
        outbound.addOutboundItem(outboundItem2);

        final Long outboundItemIdToSplit = 1L;
        final Integer quantityOfSplit = 1;
        final OutboundItemToSplit outboundItemToSplit = createOutboundItemToSplit(
                outboundItemIdToSplit,
                quantityOfSplit);

        final Outbound splittedOutbound = outbound.split(
                List.of(outboundItemToSplit));

        assertThat(splittedOutbound.getOutboundItems().size()).isEqualTo(1);
        assertThat(splittedOutbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().size()).isEqualTo(2);
        assertThat(outbound.getOutboundItems().get(0).getOutboundQuantity()).isEqualTo(1);
        assertThat(outbound.getOutboundItems().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void calculateTotalVolume() {
//        Instancio.of(OutboundItem.class)
//                        .supply(Select.field(OutboundItem::getOutboundQuantity), () -> 1)
//                                .supply(Select.field(OutboundItem::))
//        Instancio.of(Outbound.class)
//                .supply(Select.field(Outbound::getCushioningMaterial), () -> CushioningMaterial.BUBBLE_WRAP)
//                .supply(Select.field(Outbound::getCushioningMaterialQuantity), () -> 1)
//                .

    }
}