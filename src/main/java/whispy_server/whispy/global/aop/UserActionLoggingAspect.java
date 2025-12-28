package whispy_server.whispy.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.annotation.UserAction;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class UserActionLoggingAspect {

    /**
     * @UserAction 어노테이션이 붙은 메서드 실행 전후로 로깅한다.
     *
     * @param joinPoint AOP 조인 포인트
     * @param userAction UserAction 어노테이션
     * @return 메서드 실행 결과
     * @throws Throwable 메서드 실행 중 발생한 예외
     */
    @Around("@annotation(userAction)")
    public Object logUserAction(ProceedingJoinPoint joinPoint, UserAction userAction) throws Throwable {
        String userId = getUserIdentifier();
        String action = userAction.value();
        Object[] args = joinPoint.getArgs();

        log.info("[USER_ACTION] userId: {} - {} - params: {}",
                userId, action, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            log.info("[USER_ACTION] userId: {} - {} - SUCCESS", userId, action);
            return result;
        } catch (Exception e) {
            log.error("[USER_ACTION] userId: {} - {} - FAILED: {}",
                    userId, action, e.getMessage());
            throw e;
        }
    }

    /**
     * 현재 인증된 사용자 식별자를 반환한다.
     *
     * @return 사용자 이메일 또는 "anonymous"
     */
    private String getUserIdentifier() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }

        return "anonymous";
    }
}
