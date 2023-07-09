package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.common.fixture.PackagingMaterialDimensionFixture;
import leejoongseok.wms.common.fixture.PackagingMaterialFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PackagingMaterialTest {

    private PackagingMaterialDimension packagingMaterialDimension;
    private PackagingMaterial packagingMaterial;

    @BeforeEach
    void setUp() {
        packagingMaterialDimension = PackagingMaterialDimensionFixture.aPackagingMaterialDimension()
                .withInnerWidthInMillimeters(100)
                .withInnerHeightInMillimeters(100)
                .withInnerLengthInMillimeters(100)
                .withThicknessInMillimeters(1)
                .build();

        packagingMaterial = PackagingMaterialFixture.aPackagingMaterial()
                .withPackagingMaterialDimension(packagingMaterialDimension)
                .withMaxWeightInGrams(1000)
                .withName("포장자재1(최대무게 1키로 / 부피 100만)")
                .build();
    }

    @Test
    @DisplayName("입력받은 부피와 무게가 포장가능한지 확인한다.")
    void isPackageable() {
        assertPackageable(100L * 100L * 99L, 1000L, true);
        assertPackageable(100L * 100L * 100L, 1000L, false);
        assertPackageable(100L * 100L * 99L, 1001L, false);
    }

    private void assertPackageable(
            final Long volume,
            final Long weight,
            final boolean expected) {
        assertThat(packagingMaterial.isPackageable(volume, weight)).isEqualTo(expected);
    }

    @Test
    @DisplayName("포장재의 포장가능한 부피를 계산한다.[포장가능한 부피는 내부 부피 - 두께]")
    void calculatePackageableVolume() {

        final Long packageableVolume = packagingMaterial.calculatePackageableVolume();

        final long volume = 100L * 100L * 100L;
        final int thicknessVolume = 1;
        assertThat(packageableVolume).isEqualTo(volume - thicknessVolume);

    }
}