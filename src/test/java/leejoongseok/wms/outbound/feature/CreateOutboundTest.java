package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateOutboundTest {

    @Test
    @DisplayName("출고를 생성한다.")
    void createOutbound() {
        final CreateOutbound createOutbound = new CreateOutbound();

        createOutbound.request();

    }
}
