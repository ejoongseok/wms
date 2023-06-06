package leejoongseok.wms.packaging.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.packaging.domain.PackagingMaterialRepository;
import leejoongseok.wms.packaging.domain.PackagingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePackagingMaterialsTest extends ApiTest {

    @Autowired
    private CreatePackagingMaterials createPackagingMaterials;
    @Autowired
    private PackagingMaterialRepository packagingMaterialRepository;

    @Test
    @DisplayName("포장재를 등록한다.")
    void createPackagingMaterials() {
        final Integer innerWidthMillimeter = 1;
        final Integer innerHeightMillimeter = 1;
        final Integer innerLengthMillimeter = 1;
        final Integer outerWidthMillimeter = 1;
        final Integer outerHeightMillimeter = 1;
        final Integer outerLengthMillimeter = 1;
        final Integer weightInGrams = 1;
        final PackagingType packagingType = PackagingType.BOX;
        final Integer thickness = 1;
        final String name = "name";
        final String code = "code";
        final Integer maxWeightGram = 1;
        final String description = "description";

        final CreatePackagingMaterials.Request request = new CreatePackagingMaterials.Request(
                innerWidthMillimeter,
                innerHeightMillimeter,
                innerLengthMillimeter,
                outerWidthMillimeter,
                outerHeightMillimeter,
                outerLengthMillimeter,
                weightInGrams,
                packagingType,
                thickness,
                name,
                code,
                maxWeightGram,
                description
        );

        createPackagingMaterials.request(request);

        assertThat(packagingMaterialRepository.findById(1L)).isPresent();
    }
}
