package leejoongseok.wms.outbound.domain;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PackagingMaterialTest {

    @Test
    @DisplayName("입력받은 부피와 무게가 포장가능한지 확인한다.")
    void isPackageable() {
        final PackagingMaterial packagingMaterial = createPackagingMaterial(
                createPackagingMaterialDimension(
                        100,
                        100,
                        100,
                        1
                ),
                1000,
                "포장자재1(최대무게 1키로 / 부피 100만)");

        final boolean packageable = packagingMaterial.isPackageable(100L * 100L * 99L, 1000L);

        assertThat(packageable).isTrue();
    }

    @Test
    @DisplayName("입력받은 부피와 무게가 포장가능한지 확인한다.[포장가능한 부피는 내부 부피 - 두께]")
    void false_isPackageable() {
        final PackagingMaterial packagingMaterial = createPackagingMaterial(
                createPackagingMaterialDimension(
                        100,
                        100,
                        100,
                        1
                ),
                1000,
                "포장자재1(최대무게 1키로 / 부피 100만)");

        final boolean packageable = packagingMaterial.isPackageable(100L * 100L * 100L, 1000L);

        assertThat(packageable).isFalse();
    }

    @Test
    @DisplayName("입력받은 부피와 무게가 포장가능한지 확인한다.[포장가능한 무게 초과]")
    void false2_isPackageable() {
        final PackagingMaterial packagingMaterial = createPackagingMaterial(
                createPackagingMaterialDimension(
                        100,
                        100,
                        100,
                        1
                ),
                1000,
                "포장자재1(최대무게 1키로 / 부피 100만)");

        final boolean packageable = packagingMaterial.isPackageable(100L * 100L * 99L, 1001L);

        assertThat(packageable).isFalse();
    }

    private PackagingMaterial createPackagingMaterial(
            final PackagingMaterialDimension packagingMaterialDimension,
            final int maxWeightInGrams,
            final String packagingMaterialName) {
        return Instancio.of(PackagingMaterial.class)
                .supply(Select.field(PackagingMaterial::getPackagingMaterialDimension), () -> packagingMaterialDimension)
                .supply(Select.field(PackagingMaterial::getMaxWeightInGrams), () -> maxWeightInGrams)
                .supply(Select.field(PackagingMaterial::getName), () -> packagingMaterialName)
                .create();
    }

    private PackagingMaterialDimension createPackagingMaterialDimension(
            final int innerWidthInMillimeters,
            final int innerHeightInMillimeters,
            final int innerLengthInMillimeters,
            final int thicknessInMillimeters) {
        return Instancio.of(PackagingMaterialDimension.class)
                .supply(Select.field(PackagingMaterialDimension::getInnerWidthInMillimeters), () -> innerWidthInMillimeters)
                .supply(Select.field(PackagingMaterialDimension::getInnerHeightInMillimeters), () -> innerHeightInMillimeters)
                .supply(Select.field(PackagingMaterialDimension::getInnerLengthInMillimeters), () -> innerLengthInMillimeters)
                .supply(Select.field(PackagingMaterialDimension::getThicknessInMillimeters), () -> thicknessInMillimeters)
                .create();
    }

    @Test
    @DisplayName("포장재의 포장가능한 부피를 계산한다.[포장가능한 부피는 내부 부피 - 두께]")
    void calculatePackageableVolume() {
        final PackagingMaterial packagingMaterial = createPackagingMaterial(
                createPackagingMaterialDimension(
                        100,
                        100,
                        100,
                        1
                ),

                1000,
                "포장자재1(최대무게 1키로 / 부피 100만)");

        final Long packageableVolume = packagingMaterial.calculatePackageableVolume();

        final long volume = 100L * 100L * 100L;
        final int thicknessVolume = 1;
        assertThat(packageableVolume).isEqualTo(volume - thicknessVolume);

    }
}