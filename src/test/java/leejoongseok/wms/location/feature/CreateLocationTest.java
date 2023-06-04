package leejoongseok.wms.location.feature;

import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateLocationTest {
    private CreateLocation createLocation;

    @BeforeEach
    void setUp() {
        createLocation = new CreateLocation();
    }

    @Test
    @DisplayName("로케이션을 등록한다.")
    void createLocation() {
        final String locationBarcode = "A1-1-1";
        final StorageType storageType = StorageType.CELL;
        final UsagePurpose usagePurpose = UsagePurpose.STOW;

        final CreateLocation.Request request = new CreateLocation.Request(
                locationBarcode,
                storageType,
                usagePurpose
        );

        createLocation.request(request);

    }
}
