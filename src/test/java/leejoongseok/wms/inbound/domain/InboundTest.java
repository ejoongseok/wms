package leejoongseok.wms.inbound.domain;

import leejoongseok.wms.common.fixture.InboundFixture;
import leejoongseok.wms.common.fixture.InboundItemFixture;
import leejoongseok.wms.common.fixture.ItemFixture;
import leejoongseok.wms.inbound.exception.InboundItemIdNotFoundException;
import leejoongseok.wms.inbound.exception.UnconfirmedInboundException;
import leejoongseok.wms.item.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InboundTest {

    private Item item;
    private Inbound inbound;
    private InboundItem inboundItem;

    @BeforeEach
    void setUp() {
        item = ItemFixture.aDefaultItem()
                .build();

        inbound = InboundFixture.aDefaultInbound()
                .withTotalAmount(BigDecimal.valueOf(2000))
                .build();

        inboundItem = InboundItemFixture.aDefaultInboundItem()
                .withItem(item)
                .build();
        inbound.addInboundItems(List.of(inboundItem));
    }

    @Test
    @DisplayName("[실패]입고에 입고 상품을 등록한다. - 입고 총액과 입고 상품 개별 총액이 일치하지 않는 경우")
    void fail_wrong_total_amount_addInboundItems() {
        final BigDecimal wrongTotalAmount = BigDecimal.valueOf(3000000);
        final Inbound inbound = InboundFixture.aDefaultInbound()
                .withTotalAmount(wrongTotalAmount)
                .build();

        assertThatThrownBy(() -> {
            inbound.addInboundItems(List.of(inboundItem));
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("입고 상품의 총 금액이 주문 금액과 일치하지 않습니다.");
    }

    @Test
    @DisplayName("[실패]입고에 입고 상품을 등록한다. - 입고상품이 비어있는 경우")
    void fail_empty_inboundItems_addInboundItems() {
        final List<InboundItem> emptyList = List.of();

        assertThatThrownBy(() -> {
            inbound.addInboundItems(emptyList);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("입고 상품은 1개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("입고를 확정한다.")
    void confirmInspected() {
        inbound.confirmInspected();

        assertThat(inbound.getStatus()).isEqualTo(InboundStatus.CONFIRM_INSPECTED);
    }

    @Test
    @DisplayName("[실패]입고를 확정한다. - 입고를 확정할 수 있는 상태가 아님(확정은 발주 요청 시 가능)")
    void fail_invalid_status_confirmInspected() {
        inbound.confirmInspected();

        assertThatThrownBy(() -> {
            inbound.confirmInspected();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("입고 확정 할 수 있는 상태가 아닙니다. 현재 상태:[입고 확정]");
    }


    @Test
    @DisplayName("입고를 거부한다.")
    void rejectInbound() {
        inbound.reject("입고 거부 사유");

        assertThat(inbound.getStatus()).isEqualTo(InboundStatus.REJECTED);
        assertThat(inbound.getRejectionReasons()).isEqualTo("입고 거부 사유");
    }

    @Test
    @DisplayName("[실패] 입고를 거부한다. - 입고 거부 사유를 입력하지 않음.")
    void fail_empty_reason_rejectInbound() {

        assertThatThrownBy(() -> {
            inbound.reject("");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("입고 거부 사유는 필수입니다.");
    }

    @Test
    @DisplayName("[실패] 입고를 거부한다. - 입고를 거부할 수 있는 상태가 아님(거부는 발주 요청 일때만)")
    void fail_invalid_status_rejectInbound() {

        inbound.confirmInspected();

        assertThatThrownBy(() -> {
            inbound.reject("입고 거부 사유");
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("입고 거부 할 수 있는 상태가 아닙니다. 현재 상태:[입고 확정]");
    }

    @Test
    @DisplayName("LPN을 생성한다.")
    void createLPN() {
        final Long inboundItemId = 1L;
        final LocalDateTime availableExpirationAt = LocalDateTime.now().plusDays(1);
        final String lpnBarcode = "lpnBarcode";
        inbound.confirmInspected();

        final LPN lpn = inbound.createLPN(
                inboundItemId,
                lpnBarcode,
                availableExpirationAt);

        assertThat(lpn).isNotNull();
        assertThat(lpn.getLpnBarcode()).isEqualTo("lpnBarcode");
        assertThat(lpn.getExpirationAt()).isEqualTo(availableExpirationAt);
    }

    @Test
    @DisplayName("[실패] LPN을 생성한다. - 입고의 현재 상태가 LPN을 생성가능한 상태가 아닌경우.")
    void fail_inbound_invalid_status_createLPN() {
        final Long inboundItemId = 1L;
        final LocalDateTime availableExpirationAt = LocalDateTime.now().plusDays(1);
        final String lpnBarcode = "lpnBarcode";

        assertThatThrownBy(() -> {
            inbound.createLPN(
                    inboundItemId,
                    lpnBarcode,
                    availableExpirationAt);
        }).isInstanceOf(UnconfirmedInboundException.class)
                .hasMessageContaining("입고 확인이 완료되지 않은 입고 아이템에는 LPN을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("[실패] LPN을 생성한다. - LPN을 생성할 입고 아이템이 해당 입고에 속하지 않는 경우.")
    void fail_invalid_create_lpn_paramter_createLPN() {
        final Long inboundItemId = 2L;
        final LocalDateTime availableExpirationAt = LocalDateTime.now().plusDays(1);
        final String lpnBarcode = "lpnBarcode";
        inbound.confirmInspected();

        assertThatThrownBy(() -> {
            inbound.createLPN(
                    inboundItemId,
                    lpnBarcode,
                    availableExpirationAt);
        }).isInstanceOf(InboundItemIdNotFoundException.class)
                .hasMessageContaining("입고 아이템 ID [2]에 해당하는 입고 아이템을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("[실패] LPN을 생성한다. - 유통기한이 지난 경우 생성이 불가하다.")
    void fail_expired_lpn_createLPN() {
        final Long inboundItemId = 1L;
        final LocalDateTime availableExpirationAt = LocalDateTime.now().minusDays(1);
        final String lpnBarcode = "lpnBarcode";
        inbound.confirmInspected();

        assertThatThrownBy(() -> {
            inbound.createLPN(
                    inboundItemId,
                    lpnBarcode,
                    availableExpirationAt);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유통기한은 현재시간보다 미래여야 합니다.");
    }

    @Test
    @DisplayName("[실패] LPN을 생성한다. - inbound item id null")
    void fail_inbound_item_id_null_lpn_createLPN() {
        final Long null_inboundItemId = null;
        final LocalDateTime availableExpirationAt = LocalDateTime.now().minusDays(1);
        final String lpnBarcode = "lpnBarcode";
        inbound.confirmInspected();

        assertThatThrownBy(() -> {
            inbound.createLPN(
                    null_inboundItemId,
                    lpnBarcode,
                    availableExpirationAt);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("입고 상품 ID는 필수입니다.");
    }

    @Test
    @DisplayName("[실패] LPN을 생성한다. - lpn barcode null")
    void fail_lpn_barcode_null_lpn_createLPN() {
        final Long inboundItemId = 1L;
        final LocalDateTime availableExpirationAt = LocalDateTime.now().minusDays(1);
        final String null_lpnBarcode = null;
        inbound.confirmInspected();

        assertThatThrownBy(() -> {
            inbound.createLPN(
                    inboundItemId,
                    null_lpnBarcode,
                    availableExpirationAt);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("LPN 바코드는 필수입니다.");
    }

    @Test
    @DisplayName("[실패] LPN을 생성한다. - expirationAt null")
    void fail_expirationAt_null_lpn_createLPN() {
        final Long inboundItemId = 1L;
        final LocalDateTime null_ExpirationAt = null;
        final String lpnBarcode = "lpnBarcode";
        inbound.confirmInspected();

        assertThatThrownBy(() -> {
            inbound.createLPN(
                    inboundItemId,
                    lpnBarcode,
                    null_ExpirationAt);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유통기한은 필수입니다.");
    }
}