package leejoongseok.wms.inbound.domain;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InboundItemTest {

    @Test
    @DisplayName("입고 아이템의 LPN을 등록한다.")
    void assignLPN() {
        final InboundItem inboundItem = Instancio.create(InboundItem.class);
        final String lpnBarcode = "lpnBarcode";
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1);

        final LPN lpn = inboundItem.assignLPN(
                lpnBarcode,
                expirationAt
        );

        assertThat(lpn).isNotNull();
    }

    @Test
    @DisplayName("[실패] 입고 아이템의 LPN을 생성한다. - LPN 바코드가 null")
    void fail_null_lpn_barcode_assignLPN() {
        final InboundItem inboundItem = Instancio.create(InboundItem.class);
        final String invalid_lpnBarcode = null;
        final LocalDateTime expirationAt = LocalDateTime.now().plusDays(1);

        assertThatThrownBy(() -> {
            inboundItem.assignLPN(
                    invalid_lpnBarcode,
                    expirationAt
            );
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("LPN 바코드는 필수입니다.");
    }

    @Test
    @DisplayName("[실패] 입고 아이템의 LPN을 생성한다. - 유통기한이 null")
    void fail_null_expirationAt_assignLPN() {
        final InboundItem inboundItem = Instancio.create(InboundItem.class);
        final String lpnBarcode = "lpnBarcode";
        final LocalDateTime invalid_expirationAt = null;

        assertThatThrownBy(() -> {
            inboundItem.assignLPN(
                    lpnBarcode,
                    invalid_expirationAt
            );
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유통기한은 필수입니다.");
    }

    @Test
    @DisplayName("[실패] 입고 아이템의 LPN을 생성한다. - 유통기한이 지난 경우")
    void fail_expired_assignLPN() {
        final InboundItem inboundItem = Instancio.create(InboundItem.class);
        final String lpnBarcode = "lpnBarcode";
        final LocalDateTime expiredLPN = LocalDateTime.now().minusDays(1);

        assertThatThrownBy(() -> {
            inboundItem.assignLPN(
                    lpnBarcode,
                    expiredLPN
            );
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유통기한은 현재시간보다 미래여야 합니다.");
    }
}