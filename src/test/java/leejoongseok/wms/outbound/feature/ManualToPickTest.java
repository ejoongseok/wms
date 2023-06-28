package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.location.domain.LocationLPNRepository;
import leejoongseok.wms.outbound.domain.PickingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualToPickTest {

    private ManualToPick manualToPick;
    private PickingRepository pickingRepository;
    private LocationLPNRepository locationLPNRepository;

    @BeforeEach
    void setUp() {
        pickingRepository = null;
        locationLPNRepository = null;
        manualToPick = new ManualToPick(
                pickingRepository,
                locationLPNRepository);
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

        //pickingRepository.findById(1L).get().getPickedQuantity().ast(pickedQuantity);
    }
}