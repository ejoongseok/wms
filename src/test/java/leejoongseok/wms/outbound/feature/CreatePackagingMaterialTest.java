package leejoongseok.wms.outbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.PackagingMaterialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePackagingMaterialTest extends ApiTest {

    @Autowired
    private PackagingMaterialRepository packagingMaterialRepository;

    @Test
    @DisplayName("포장재를 등록한다.")
    void createPackagingMaterial() {
        Scenario
                .createPackagingMaterial().request();

        assertThat(packagingMaterialRepository.findById(1L)).isPresent();
    }
}
