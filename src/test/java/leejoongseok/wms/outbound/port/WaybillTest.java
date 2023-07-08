package leejoongseok.wms.outbound.port;

import leejoongseok.wms.outbound.domain.Outbound;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WaybillTest {

    private Waybill waybill;

    @BeforeEach
    void setUp() {
        waybill = new Waybill();
    }

    @Test
    @DisplayName("출고에대한 운송장을 발행한다.")
    void request() {
        final Outbound outbound = Instancio.of(Outbound.class)
                .ignore(Select.field(Outbound::getTrackingNumber))
                .create();

        final String trackingNumber = waybill.request(outbound);

        assertThat(trackingNumber).isNotNull();
    }

    @Test
    @DisplayName("출고에대한 운송장을 발행한다. - 이미 운송장이 발행된 경우 예외가 발생한다.")
    void request_exists_trackingNumber() {
        final Outbound outbound = Instancio.create(Outbound.class);

        assertThatThrownBy(() -> {
            waybill.request(outbound);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 운송장이 발행되었습니다.");
    }
}