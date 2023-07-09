package leejoongseok.wms.common.fixture;

import leejoongseok.wms.outbound.domain.PackagingMaterial;
import leejoongseok.wms.outbound.domain.PackagingMaterialDimension;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class PackagingMaterialFixture {

    private InstancioApi<PackagingMaterial> packagingMaterialInstance = Instancio.of(PackagingMaterial.class);

    public static PackagingMaterialFixture aPackagingMaterial() {
        return new PackagingMaterialFixture();
    }

    public PackagingMaterialFixture withPackagingMaterialDimension(final PackagingMaterialDimension packagingMaterialDimension) {
        packagingMaterialInstance.supply(Select.field(PackagingMaterial::getPackagingMaterialDimension), () -> packagingMaterialDimension);
        return this;
    }

    public PackagingMaterialFixture withMaxWeightInGrams(final Integer maxWeightInGrams) {
        packagingMaterialInstance.supply(Select.field(PackagingMaterial::getMaxWeightInGrams), () -> maxWeightInGrams);
        return this;
    }

    public PackagingMaterialFixture withName(final String name) {
        packagingMaterialInstance.supply(Select.field(PackagingMaterial::getName), () -> name);
        return this;
    }

    public PackagingMaterial build() {
        packagingMaterialInstance = null == packagingMaterialInstance ? Instancio.of(PackagingMaterial.class) : packagingMaterialInstance;
        return packagingMaterialInstance.create();
    }
}
