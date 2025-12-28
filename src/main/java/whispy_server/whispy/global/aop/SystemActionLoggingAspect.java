package whispy_server.whispy.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.annotation.SystemAction;

import java.util.Arrays;

/**
 * @SystemAction 어노테이션이 붙은 메서드의 시스템 이벤트를 로깅하는 Aspect.
 *
 * 외부 시스템 이벤트, 스케줄러, 배치 작업 등 시스템 내부에서 자동으로 실행되는 작업을 추적합니다.
 * 사용자가 직접 시작한 작업은 UserActionLoggingAspect에서 처리됩니다.
 */
@Slf4j
@Component
@Aspect
public class SystemActionLoggingAspect {

    /**
     * @SystemAction 어노테이션이 붙은 메서드 실행 전후로 로깅한다.
     *
     * @param joinPoint AOP 조인 포인트
     * @param systemAction SystemAction 어노테이션
     * @return 메서드 실행 결과
     * @throws Throwable 메서드 실행 중 발생한 예외
     */
    @Around("@annotation(systemAction)")
    public Object logSystemAction(ProceedingJoinPoint joinPoint, SystemAction systemAction) throws Throwable {
        String action = systemAction.value();
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().toShortString();

        log.info("[SYSTEM_ACTION] {} - params: {}",
                action, Arrays.toString(args));

        try {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            log.info("[SYSTEM_ACTION] {} - SUCCESS ({}ms)", action, duration);
            return result;
        } catch (Exception e) {
            log.error("[SYSTEM_ACTION] {} - FAILED - method: {}",
                    action, methodName, e);
            throw e;
        }
    }
}
