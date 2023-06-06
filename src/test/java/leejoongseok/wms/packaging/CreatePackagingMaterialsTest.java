package leejoongseok.wms.packaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreatePackagingMaterialsTest {

    private CreatePackagingMaterials createPackagingMaterials;

    @BeforeEach
    void setUp() {
        createPackagingMaterials = new CreatePackagingMaterials();
    }

    @Test
    @DisplayName("포장재를 등록한다.")
    void createPackagingMaterials() {
        createPackagingMaterials.request();
    }
}
