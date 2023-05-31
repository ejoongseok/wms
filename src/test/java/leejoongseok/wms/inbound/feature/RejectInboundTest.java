package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import leejoongseok.wms.inbound.domain.InboundStatus;
import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RejectInboundTest {
    InboundRepository inboundRepository;

    @BeforeEach
    void setUp() {
        inboundRepository = Mockito.mock(InboundRepository.class);
    }

    @Test
    @DisplayName("입고를 거부한다.")
    void rejectInbound() {
        final Inbound inbound = Instancio.of(Inbound.class)
                .supply(Select.field(Inbound::getStatus), () -> InboundStatus.ORDER_REQUESTED)
                .create();
        Mockito.when(inboundRepository.findById(1L)).thenReturn(Optional.of(inbound));

        final RejectInbound rejectInbound = new RejectInbound(inboundRepository);

        final long inboundId = 1L;
        final String rejectionReasons = BaseFaker.instance().lorem().sentence(10);
        final RejectInbound.Request request = new RejectInbound.Request(rejectionReasons);

        rejectInbound.request(inboundId, request);

        assertThat(inbound.getStatus()).isEqualTo(InboundStatus.REJECTED);

    }
}
