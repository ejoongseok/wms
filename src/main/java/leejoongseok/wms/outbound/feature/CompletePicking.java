package leejoongseok.wms.outbound.feature;

import jakarta.validation.constraints.NotNull;

/**
 * 출고해야할 상품의 집품이 모두 완료되면 집품완료를 할 수 있다.
 */
public class CompletePicking {


    public void request(final Request request) {

    }

    public record Request(
            @NotNull(message = "집품을 완료하려는 출고 ID는 필수 입니다.")
            Long outboundId) {
    }
}
