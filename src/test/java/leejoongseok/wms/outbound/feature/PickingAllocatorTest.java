package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.outbound.domain.Outbound;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PickingAllocatorTest {

    private PickingAllocator pickingAllocator;

    @BeforeEach
    void setUp() {
        pickingAllocator = new PickingAllocator();
    }

    @Test
    @DisplayName("출고에 집품목록을 할당한다.")
    void allocate() {
        final Outbound outbound = Instancio.create(Outbound.class);

        pickingAllocator.allocate(outbound);

        assertThat(outbound.isPickingProgress()).isTrue();
    }
}