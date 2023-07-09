package leejoongseok.wms.common.fixture;

import leejoongseok.wms.outbound.domain.PackagingMaterialDimension;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class PackagingMaterialDimensionFixture {

    private InstancioApi<PackagingMaterialDimension> packagingMaterialDimensionInstance = Instancio.of(PackagingMaterialDimension.class);

    public static PackagingMaterialDimensionFixture aPackagingMaterialDimension() {
        return new PackagingMaterialDimensionFixture();
    }

    public PackagingMaterialDimensionFixture withInnerWidthInMillimeters(final Integer innerWidthInMillimeters) {
        packagingMaterialDimensionInstance.supply(Select.field(PackagingMaterialDimension::getInnerWidthInMillimeters), () -> innerWidthInMillimeters);
        return this;
    }

    public PackagingMaterialDimensionFixture withInnerHeightInMillimeters(final Integer innerHeightInMillimeters) {
        packagingMaterialDimensionInstance.supply(Select.field(PackagingMaterialDimension::getInnerHeightInMillimeters), () -> innerHeightInMillimeters);
        return this;
    }

    public PackagingMaterialDimensionFixture withInnerLengthInMillimeters(final Integer innerLengthInMillimeters) {
        packagingMaterialDimensionInstance.supply(Select.field(PackagingMaterialDimension::getInnerLengthInMillimeters), () -> innerLengthInMillimeters);
        return this;
    }

    public PackagingMaterialDimensionFixture withThicknessInMillimeters(final Integer thicknessInMillimeters) {
        packagingMaterialDimensionInstance.supply(Select.field(PackagingMaterialDimension::getThicknessInMillimeters), () -> thicknessInMillimeters);
        return this;
    }

    public PackagingMaterialDimension build() {
        packagingMaterialDimensionInstance = null == packagingMaterialDimensionInstance ? Instancio.of(PackagingMaterialDimension.class) : packagingMaterialDimensionInstance;
        return packagingMaterialDimensionInstance.create();
    }
}
