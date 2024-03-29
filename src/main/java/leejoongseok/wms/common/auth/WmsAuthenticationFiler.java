package leejoongseok.wms.common.auth;

import com.google.common.net.HttpHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class WmsAuthenticationFiler extends OncePerRequestFilter {
    private final UserIdTokenProvider userIdTokenProvider;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final UserIdToken userIdToken = userIdTokenProvider.parseToken(authorization);
        if (userIdToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        final Authentication authentication = createAuthentication(userIdToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private Authentication createAuthentication(final UserIdToken userIdToken) {
        return new UsernamePasswordAuthenticationToken(userIdToken, null, Set.of(() -> "ROLE_USER"));
    }
}
