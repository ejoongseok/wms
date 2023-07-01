package leejoongseok.wms.location.feature;

import leejoongseok.wms.location.domain.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoveToTargetChildLocationTest {

    private MoveToTargetChildLocation moveToTargetChildLocation;
    private LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        locationRepository = null;
        moveToTargetChildLocation = new MoveToTargetChildLocation(locationRepository);
    }

    @Test
    @DisplayName("대상 로케이션의 하위 로케이션으로 현재 로케이션을 이동한다.")
    void moveToTargetChildLocation() {
        final MoveToTargetChildLocation.Request request = new MoveToTargetChildLocation.Request(
                "currentLocationBarcode",
                "targetLocationBarcode"
        );
        moveToTargetChildLocation.request(request);
    }
}