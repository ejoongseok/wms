package leejoongseok.wms.packaging;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.packaging.domain.PackagingType;

public class CreatePackagingMaterials {
    public void request(final Request request) {

    }

    public record Request(
            @Min(value = 1, message = "내부 폭은 1mm 이상이어야 합니다.")
            Integer innerWidthMillimeter,
            @Min(value = 1, message = "내부 높이는 1mm 이상이어야 합니다.")
            Integer innerHeightMillimeter,
            @Min(value = 1, message = "내부 길이는 1mm 이상이어야 합니다.")
            Integer innerLengthMillimeter,
            @Min(value = 1, message = "외부 폭은 1mm 이상이어야 합니다.")
            Integer outerWidthMillimeter,
            @Min(value = 1, message = "외부 높이는 1mm 이상이어야 합니다.")
            Integer outerHeightMillimeter,
            @Min(value = 1, message = "외부 길이는 1mm 이상이어야 합니다.")
            Integer outerLengthMillimeter,
            @Min(value = 1, message = "무게는 1g 이상이어야 합니다.")
            Integer weightInGrams,
            @NotNull(message = "포장재 종류는 필수입니다.")
            PackagingType packagingType,
            @Min(value = 1, message = "두께는 1mm 이상이어야 합니다.")
            Integer thickness,
            @NotBlank(message = "포장재 이름은 필수입니다.")
            String name,
            String code,
            @Min(value = 1, message = "제한 무게는 1g 이상이어야 합니다.")
            Integer maxWeightGram,
            String description
    ) {
    }
}
