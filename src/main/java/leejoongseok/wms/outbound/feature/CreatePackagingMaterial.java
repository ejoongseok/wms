package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.outbound.domain.PackagingMaterial;
import leejoongseok.wms.outbound.domain.PackagingMaterialDimension;
import leejoongseok.wms.outbound.domain.PackagingMaterialRepository;
import leejoongseok.wms.outbound.domain.PackagingType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 출고상품의 포장에 사용할 포장재를 생성하는 컨트롤러 클래스.
 */
@RestController
@RequiredArgsConstructor
public class CreatePackagingMaterial {
    private final PackagingMaterialRepository packagingMaterialRepository;

    @Transactional
    @PostMapping("/packaging-materials")
    @ResponseStatus(HttpStatus.CREATED)
    public void request(@RequestBody @Valid final Request request) {
        final PackagingMaterial packagingMaterial = request.toEntity();

        packagingMaterialRepository.save(packagingMaterial);
    }

    public record Request(
            @Min(value = 1, message = "내부 폭은 1mm 이상이어야 합니다.")
            Integer innerWidthInMillimeters,
            @Min(value = 1, message = "내부 높이는 1mm 이상이어야 합니다.")
            Integer innerHeightInMillimeters,
            @Min(value = 1, message = "내부 길이는 1mm 이상이어야 합니다.")
            Integer innerLengthInMillimeters,
            @Min(value = 1, message = "외부 폭은 1mm 이상이어야 합니다.")
            Integer outerWidthInMillimeters,
            @Min(value = 1, message = "외부 높이는 1mm 이상이어야 합니다.")
            Integer outerHeightInMillimeters,
            @Min(value = 1, message = "외부 길이는 1mm 이상이어야 합니다.")
            Integer outerLengthInMillimeters,
            @Min(value = 1, message = "무게는 1g 이상이어야 합니다.")
            Integer weightInGrams,
            @NotNull(message = "포장재 종류는 필수입니다.")
            PackagingType packagingType,
            @Min(value = 1, message = "두께는 1mm 이상이어야 합니다.")
            Integer thickness,
            @NotBlank(message = "포장재 이름은 필수입니다.")
            String name,
            String code,
            @Min(value = 1, message = "최대 무게는 1g 이상이어야 합니다.")
            Integer maxWeightInGrams,
            String description
    ) {
        public PackagingMaterial toEntity() {
            return new PackagingMaterial(
                    new PackagingMaterialDimension(
                            innerWidthInMillimeters,
                            innerHeightInMillimeters,
                            innerLengthInMillimeters,
                            outerWidthInMillimeters,
                            outerHeightInMillimeters,
                            outerLengthInMillimeters,
                            thickness
                    ),
                    weightInGrams,
                    packagingType,
                    name,
                    code,
                    maxWeightInGrams,
                    description
            );
        }
    }
}
