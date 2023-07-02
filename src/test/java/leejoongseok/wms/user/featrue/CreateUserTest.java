package leejoongseok.wms.user.featrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateUserTest {

    private CreateUser createUser;

    @BeforeEach
    void setUp() {
        createUser = new CreateUser();
    }

    @Test
    @DisplayName("사용자를 생성한다.")
    void createUser() {
        final CreateUser.Request request = new CreateUser.Request(

        );
        createUser.request(request);
    }
}