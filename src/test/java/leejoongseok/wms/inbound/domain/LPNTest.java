package leejoongseok.wms.inbound.domain;

import leejoongseok.wms.common.fixture.LPNFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LPNTest {
    private LPN lpn;
    private LocalDateTime today;

    @BeforeEach
    void setUp() {
        today = LocalDateTime.now();
        lpn = LPNFixture.aLPN()
                .withExpirationAt(today)
                .build();
    }

    @Test
    @DisplayName("LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다.")
    void isFreshBy() {
        assertThat(lpn.isFreshBy(today.minusDays(1))).isTrue();
        assertThat(lpn.isFreshBy(today.plusDays(1))).isFalse();
    }

}