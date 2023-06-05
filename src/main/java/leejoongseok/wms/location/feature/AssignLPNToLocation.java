package leejoongseok.wms.location.feature;

import jakarta.validation.constraints.NotBlank;

public class AssignLPNToLocation {
    public void request(final Request request) {

    }

    public record Request(
            @NotBlank(message = "로케이션에 등록할 LPN 바코드를 입력해주세요.")
            String lpnBarcode,
            @NotBlank(message = "로케이션 바코드를 입력해주세요.")
            String locationBarcode) {
    }
}
