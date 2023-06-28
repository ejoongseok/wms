package leejoongseok.wms.outbound.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualToPickTest {

    private ManualToPick manualToPick;

    @BeforeEach
    void setUp() {
        manualToPick = new ManualToPick();
    }

    @Test
    void manualToPick() {
        final Long pickingId = 1L;
        final Long locationLPNId = 1L;
        final Integer pickedQuantity = 10;
        final ManualToPick.Request request = new ManualToPick.Request(
                pickingId,
                locationLPNId,
                pickedQuantity
        );

        manualToPick.request(request);


    }
}