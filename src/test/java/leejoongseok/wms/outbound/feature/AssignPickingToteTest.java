package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AssignPickingToteTest {

    private AssignPickingTote assignPickingTote;

    @BeforeEach
    void setUp() {
        assignPickingTote = new AssignPickingTote();
    }

    @Test
    @DisplayName("출고를 집품할 토트를 할당한다.")
    void assignPickingTote() {
        final Long outboundId = 1L;
        final String toteBarcode = "TOTE0001";
        final AssignPickingTote.Request request = new AssignPickingTote.Request(
                outboundId,
                toteBarcode);

        assignPickingTote.request(request);

        // TODO outboundId로 출고를 조회하여 토트가 할당되었는지 확인한다.
    }
}
