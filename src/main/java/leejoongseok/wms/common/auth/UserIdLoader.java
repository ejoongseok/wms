package leejoongseok.wms.common.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdLoader {
    public Long getUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserIdToken userIdToken = (UserIdToken) authentication.getPrincipal();
        return userIdToken.getUserId();
    }
}
