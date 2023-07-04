package leejoongseok.wms.common.user;

import leejoongseok.wms.common.auth.UserIdLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserIdAuditorAware implements AuditorAware<Long> {
    private final UserIdLoader userIdLoader;

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(userIdLoader.getUserId());
    }
}
