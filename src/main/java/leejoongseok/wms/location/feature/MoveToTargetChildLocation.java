package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.NotBlank;

/**
 * 대상 로케이션의 하위 로케이션으로 현재 로케이션을 이동하는 기능
 * ex) B 파레트에 A 토트를 올리는 행위.
 * 파레트에는 물리적으로 여러개의 토트가 존재할 수 있다.
 * 렉에는 물리적으로 여러개의 파레트가 존재할 수 있다.
 * 토트,파레트,렉 같은 물리적인것뿐 아니라 어떠한 장소(구역) 혹은 논리적 위치도 로케이션이 될 수 있다.
 * 로케이션은 바구니 같은것 바구니 안에 사과와 오렌지가 들어갈 수 있고
 * 과일을 담은 바구니를 더 큰 바구니 혹은 어떤 선반같은데 올릴수 있음.
 * 더 큰 바구니도 로케이션이고 선반도 로케이션이다.
 */
public class MoveToTargetChildLocation {
    public void request(final Request request) {
        throw new UnsupportedOperationException("Unsupported request");
    }

    public record Request(
            @NotBlank(message = "현재 로케이션 바코드는 필수입니다.")
            String currentLocationBarcode,
            @NotBlank(message = "이동 대상 로케이션 바코드는 필수입니다.")
            String targetLocationBarcode) {
    }
}
