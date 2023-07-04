package leejoongseok.wms.common.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

class UserActionAspectTest {

    @Test
    void name() {
        final LocalDateTime start = LocalDateTime.of(2021, 1, 1, 0, 0, 5);
        final LocalDateTime end = LocalDateTime.of(2021, 1, 1, 0, 0, 7);

        final TemporalAmount between = java.time.Duration.between(start, end);

        // get between milliseconds
        final long millis = between.get(java.time.temporal.ChronoUnit.MILLIS);
    }
}