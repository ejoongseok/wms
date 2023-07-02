package leejoongseok.wms.common.workload;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MeasureWorkLoadAspect {
    private final WorkLoadRepository workLoadRepository;

    @Pointcut("@annotation(leejoongseok.wms.common.workload.MeasureWorkLoad)")
    public void measureWorkLoadPointcut() {
    }

    @Around("measureWorkLoadPointcut()")
    public Object measureWorkLoad(final ProceedingJoinPoint joinPoint) throws Throwable {
        final MeasureWorkLoad measureWorkLoad = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(MeasureWorkLoad.class);
        final WorkloadType type = measureWorkLoad.type();

        final Object proceed = joinPoint.proceed();

        final WorkLoad workLoad = new WorkLoad(type);
        workLoadRepository.save(workLoad);

        return proceed;
    }
}
