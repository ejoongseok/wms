package leejoongseok.wms.common.user;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserActionAspect {

    @Around("execution(* leejoongseok.wms..*.feature..*.*(..))")
    public Object logUserAction(final ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}
