package leejoongseok.wms.user.featrue;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserTest extends ApiTest {

    @Autowired
    private CreateUser createUser;
    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("사용자를 생성한다.")
    void createUser() {
        final String name = "이중석";
        final CreateUser.Request request = new CreateUser.Request(
                name
        );
        createUser.request(request);

        assertThat(userRepository.findAll()).hasSize(1);
    }
}