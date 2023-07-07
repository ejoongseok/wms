package leejoongseok.wms.common.retry;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 재고 변경같은 정합성 유지가 필요한 작업에서 OptimisticLockException이 발생하면 재시도하는 Aspect
 * spring retry를 직접 구현한 기능 https://www.baeldung.com/spring-retry
 * 아직 retry를 사용할만한곳이 많지 않다고 판단되서 직접구현
 */
@Slf4j
@Aspect
@Component
public class RetryOnOptimisticLockingAspect {

    @Around("@annotation(RetryOnOptimisticLockingFailure)")
    public Object doConcurrentOperation(final ProceedingJoinPoint pjp) throws Throwable {
        int numAttempts = 0;
        final RetryOnOptimisticLockingFailure annotation = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(RetryOnOptimisticLockingFailure.class);
        final int maxRetries = annotation.maxRetries();

        do {
            numAttempts++;
            try {
                return pjp.proceed();
            } catch (final OptimisticLockException oe) {
                if (numAttempts > maxRetries) {
                    log.info("RetryOnOptimisticLockingFailure: max retries exceeded");
                    throw oe;
                }
                Thread.sleep(1000); // 재시도 전 지연
            }
        } while (numAttempts <= maxRetries);

        // 이 부분은 실행되지 않는다.
        return null;
    }
}
