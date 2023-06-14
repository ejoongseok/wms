package leejoongseok.wms.inbound.domain;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LPNTest {

    @Test
    @DisplayName("LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다. [해당일자는 유통기한 전임.]")
    void isFreshBy() {
        final LocalDateTime today = LocalDateTime.now();
        final LPN lpn = Instancio.of(LPN.class)
                .supply(Select.field(LPN::getExpirationAt), () -> today)
                .create();

        final boolean isFresh = lpn.isFreshBy(today.minusDays(1));

        assertThat(isFresh).isTrue();
    }

    @Test
    @DisplayName("LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다. [해당일자는 유통기한 후임.]")
    void isFreshBy2() {
        final LocalDateTime today = LocalDateTime.now();
        final LPN lpn = Instancio.of(LPN.class)
                .supply(Select.field(LPN::getExpirationAt), () -> today)
                .create();

        final boolean isFresh = lpn.isFreshBy(today.plusDays(1));

        assertThat(isFresh).isFalse();
    }
}