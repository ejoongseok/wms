package leejoongseok.wms.outbound.feature;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundRepository;
import leejoongseok.wms.outbound.domain.PackagingMaterial;
import leejoongseok.wms.outbound.domain.PackagingMaterialRepository;
import leejoongseok.wms.outbound.exception.OutboundIdNotFoundException;
import leejoongseok.wms.outbound.exception.PackagingMaterialIdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AssignPacking {
    private final OutboundRepository outboundRepository;
    private final PackagingMaterialRepository packagingMaterialRepository;

    @Transactional
    @PostMapping("/outbounds/{outboundId}/packings")
    public void request(
            @PathVariable final Long outboundId,
            @RequestBody @Valid final Request request) {
        final Outbound outbound = getOutbound(outboundId);
        final PackagingMaterial packagingMaterial = getPackagingMaterial(request);
        compareActualAndRecommendedPackaging(packagingMaterial, outbound);

        outbound.assignPacking(packagingMaterial, request.packagingWeightInGrams);
    }

    /**
     * 실제 포장자재와 추천 포장자재를 비교하여 다른 경우 분석용 로그 남김.
     */
    private void compareActualAndRecommendedPackaging(
            final PackagingMaterial packagingMaterial,
            final Outbound outbound) {
        final PackagingMaterial recommendedPackagingMaterial = outbound.getRecommendedPackagingMaterial();
        if (!recommendedPackagingMaterial.equals(packagingMaterial)) {
            log.info("추천 포장자재[{}]와 포장자재[{}]가 다릅니다. 출고 ID[{}]",
                    recommendedPackagingMaterial.getId(), packagingMaterial.getId(), outbound.getId());
        }
    }

    private Outbound getOutbound(final Long outboundId) {
        return outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundIdNotFoundException(outboundId));
    }

    private PackagingMaterial getPackagingMaterial(final Request request) {
        return packagingMaterialRepository.findById(request.packagingMaterialId)
                .orElseThrow(() -> new PackagingMaterialIdNotFoundException(request.packagingMaterialId));
    }

    public record Request(
            @NotNull(message = "포장자재 ID는 필수 입니다.")
            Long packagingMaterialId,
            @NotNull(message = "실중량은 필수 입니다.")
            @Min(value = 1, message = "실중량은 1g 이상이어야 합니다.")
            Integer packagingWeightInGrams) {
    }
}
