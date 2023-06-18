package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.domain.PackagingMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SplitToOutboundTest {

    private SplitToOutbound splitToOutbound;
    private OutboundRepository outboundRepository;
    private PackagingMaterialRepository packagingMaterialRepository;

    @BeforeEach
    void setUp() {
        outboundRepository = null;
        packagingMaterialRepository = null;
        splitToOutbound = new SplitToOutbound(outboundRepository, packagingMaterialRepository);
    }

    @Test
    @DisplayName("대기중인 출고건을 분할한다.")
    void splitToOutbound() {
        final Integer cushioningMaterialQuantity = 1;
        final Long outBoundIdToSplit = 1L;
        final List<SplitToOutbound.Request.Item> itemsToSplit = List.of(
                new SplitToOutbound.Request.Item(1L, 1),
                new SplitToOutbound.Request.Item(2L, 1)
        );
        final SplitToOutbound.Request request = new SplitToOutbound.Request(
                cushioningMaterialQuantity,
                outBoundIdToSplit,
                itemsToSplit
        );

        splitToOutbound.request(request);
    }
}
