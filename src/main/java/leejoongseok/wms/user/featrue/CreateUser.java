package leejoongseok.wms.user.featrue;

import jakarta.validation.constraints.NotBlank;

public class CreateUser {
    public void request(final Request request) {
        throw new UnsupportedOperationException("Unsupported request");
    }

    public record Request(
            @NotBlank(message = "사용자 이름은 필수입니다.")
            String name) {
    }
}
