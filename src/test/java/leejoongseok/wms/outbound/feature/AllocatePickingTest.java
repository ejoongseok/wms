package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AllocatePickingTest extends ApiTest {


    @Autowired
    private AllocatePicking allocatePicking;

    @Autowired
    private OutboundRepository outboundRepository;

    @Test
    @DisplayName("출고 상품에 대한 집품 목록을 할당한다.")
    void allocatePicking() {
        final Long outboundId = 1L;
        final AllocatePicking.Request request = new AllocatePicking.Request(outboundId);

        allocatePicking.request(request);
    }
}
