package leejoongseok.wms.user.featrue;

import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.user.domain.User;
import leejoongseok.wms.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CreateUser {
    private final UserRepository userRepository;

    public void request(final Request request) {
        final User user = request.toEntity();
        userRepository.save(user);
    }

    public record Request(
            @NotBlank(message = "사용자 이름은 필수입니다.")
            String name) {

        User toEntity() {
            return new User(name);
        }
    }
}
