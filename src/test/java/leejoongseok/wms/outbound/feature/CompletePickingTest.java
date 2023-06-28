package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CompletePickingTest {

    private CompletePicking completePicking;

    @BeforeEach
    void setUp() {
        completePicking = new CompletePicking();
    }

    @Test
    @DisplayName("출고해야할 상품의 집품이 모두 완료되면 집품완료를 할 수 있다.")
    void completePicking() {
        final Long outboundId = 1L;
        final CompletePicking.Request request = new CompletePicking.Request(
                outboundId
        );

        completePicking.request(request);

        // outboundRepository.findById(1L).get().getStatus() == PickingCompleted
    }
}