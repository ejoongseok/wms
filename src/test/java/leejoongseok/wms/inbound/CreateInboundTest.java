package leejoongseok.wms.inbound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateInboundTest {

    @Test
    @DisplayName("입고를 등록한다.")
    void createInbound() {
        final CreateInbound createInbound = new CreateInbound();
        final CreateInbound.Request request = new CreateInbound.Request();

        createInbound.request(request);
    }
}
