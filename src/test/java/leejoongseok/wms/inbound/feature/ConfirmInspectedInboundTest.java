package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.inbound.domain.Inbound;
import leejoongseok.wms.inbound.domain.InboundRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class ConfirmInspectedInboundTest {

    private ConfirmInspectedInbound confirmInspectedInbound;
    private InboundRepository inboundRepository;

    @BeforeEach
    void setUp() {
        inboundRepository = Mockito.mock(InboundRepository.class);
        confirmInspectedInbound = new ConfirmInspectedInbound(inboundRepository);
    }

    @Test
    @DisplayName("입고를 확정한다.")
    void confirmInspectedInbound() {
        final long inboundId = 1L;
        final Inbound inbound = Instancio.create(Inbound.class);
        Mockito.when(inboundRepository.findById(inboundId)).thenReturn(Optional.of(inbound));

        confirmInspectedInbound.request(inboundId);

    }
}
