package leejoongseok.wms.common.user;

import jakarta.servlet.http.HttpServletRequest;
import leejoongseok.wms.common.auth.UserIdLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserActionAspect {
    private final UserIdLoader userIdLoader;
    private final UserActionHistoryRepository userActionHistoryRepository;

    @Around("execution(* leejoongseok.wms..*.feature..*.*(..))")
    public Object logUserAction(final ProceedingJoinPoint pjp) throws Throwable {
        final LocalDateTime startTime = LocalDateTime.now();

        try {
            return pjp.proceed();
        } finally {
            saveUserActionHistory(pjp, startTime);
        }
    }

    private void saveUserActionHistory(final ProceedingJoinPoint pjp, final LocalDateTime startTime) {
        try {
            final HttpServletRequest request = getRequest();
            if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) return;
            final String featureName = pjp.getSignature().getDeclaringType().getSimpleName();
            final UserActionHistory userActionHistory = createUserActionHistory(
                    featureName,
                    request,
                    startTime);
            userActionHistory.registerProcessTime();
            userActionHistoryRepository.save(userActionHistory);
        } catch (final RuntimeException e) {
            log.error("UserActionHistory 저장 실패", e);
        }
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private UserActionHistory createUserActionHistory(final String featureName, final HttpServletRequest request, final LocalDateTime startTime) {
        return new UserActionHistory(
                featureName,
                request.getRequestURI(),
                getQueryParameters(request),
                getRequestBody(request),
                getHeaders(request),
                userIdLoader.getUserId(),
                startTime
        );
    }

    private String getQueryParameters(final HttpServletRequest request) {
        // 요청 파라미터를 추출한다.
        final Enumeration<String> params = request.getParameterNames();
        final Map<String, String> queryParameterMap = new HashMap<>();
        // 요청 파라미터 중 requestBody를 제외한 나머지를 추출한다.
        while (params.hasMoreElements()) {
            final String param = params.nextElement();
            if (!"requestBody".equals(param)) {
                final String queryParameter = request.getParameter(param);
                queryParameterMap.put(param, queryParameter);
            }
        }
        return CollectionUtils.isEmpty(queryParameterMap) ? null : queryParameterMap.toString();
    }

    private String getRequestBody(final HttpServletRequest request) {
        // 요청 파라미터를 추출한다.
        final Enumeration<String> params = request.getParameterNames();
        // 요청 파라미터 중 requestBody를 추출한다.
        while (params.hasMoreElements()) {
            final String param = params.nextElement();
            if ("requestBody".equals(param)) {
                return request.getParameter(param);
            }
        }
        return null;
    }
    // 요청 쿼리 파라미터를 추출한다.

    private String getHeaders(final HttpServletRequest request) {
        // 헤더 정보를 담을 Map
        final Map<String, String> headersMap = new HashMap<>();
        final Enumeration<String> headerNames = request.getHeaderNames();

        // 헤더 정보 추출
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            final String headerValue = request.getHeader(headerName);
            headersMap.put(headerName, headerValue);
        }
        return headersMap.toString();
    }
}
