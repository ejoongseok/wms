package leejoongseok.wms.location.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoveToTargetChildLocationTest {

    private MoveToTargetChildLocation moveToTargetChildLocation;

    @BeforeEach
    void setUp() {
        moveToTargetChildLocation = new MoveToTargetChildLocation();
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