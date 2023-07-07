package leejoongseok.wms.location.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.location.domain.LocationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CreateLocationTest extends ApiTest {
    @Autowired
    private LocationRepository locationRepository;

    @Test
    @DisplayName("로케이션을 등록한다.")
    void createLocation() {
        Scenario
                .createLocation().request();

        assertThat(locationRepository.findById(1L)).isPresent();
    }
}
