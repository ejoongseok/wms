package leejoongseok.wms.user.featrue;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import leejoongseok.wms.user.domain.User;
import leejoongseok.wms.user.domain.UserRepository;
import leejoongseok.wms.user.exception.UserNameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


@Component
@RequiredArgsConstructor
public class CreateUser {
    private final UserRepository userRepository;

    @Transactional
    public void request(@RequestBody @Valid final Request request) {
        validateRequest(request.name);
        final User user = request.toEntity();
        userRepository.save(user);
    }

    private void validateRequest(final String name) {
        userRepository.findByName(name).ifPresent(user -> {
            throw new UserNameAlreadyExistsException(name);
        });
    }

    public record Request(
            @NotBlank(message = "사용자 이름은 필수입니다.")
            String name) {

        User toEntity() {
            return new User(name);
        }
    }
}
