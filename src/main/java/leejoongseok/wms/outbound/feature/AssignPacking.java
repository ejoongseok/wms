package leejoongseok.wms.outbound.feature;

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

@Slf4j
@RequiredArgsConstructor
public class AssignPacking {
    private final OutboundRepository outboundRepository;
    private final PackagingMaterialRepository packagingMaterialRepository;

    public void request(final Request request) {
        final Outbound outbound = getOutbound(request.outboundId);
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
            @NotNull(message = "출고 ID는 필수 입니다.")
            Long outboundId,
            @NotNull(message = "포장자재 ID는 필수 입니다.")
            Long packagingMaterialId,
            @NotNull(message = "실중량은 필수 입니다.")
            @Min(value = 1, message = "실중량은 1g 이상이어야 합니다.")
            Integer packagingWeightInGrams) {
    }
}
