package whispy_server.whispy.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.annotation.AdminAction;
import whispy_server.whispy.global.utils.security.SecurityUtil;

import java.util.Arrays;

/**
 * AdminAction 어노테이션이 붙은 메서드의 관리자 행동을 로깅하는 Aspect.
 *
 * 관리자 ID, 행동 내용, 파라미터, 성공/실패 여부를 자동으로 로깅합니다.
 * 로그 형식: [ADMIN_ACTION] adminId: {id} - {action} - params: {parameters}
 */
@Slf4j
@Aspect
@Component
public class AdminActionLoggingAspect {

    /**
     * AdminAction 어노테이션이 붙은 메서드를 가로채어 관리자 행동을 로깅합니다.
     *
     * @param joinPoint AOP 조인 포인트
     * @param adminAction AdminAction 어노테이션
     * @return 메서드 실행 결과
     * @throws Throwable 메서드 실행 중 발생한 예외
     */
    @Around("@annotation(adminAction)")
    public Object logAdminAction(ProceedingJoinPoint joinPoint, AdminAction adminAction) throws Throwable {
        String adminId = SecurityUtil.getCurrentUserIdentifier();
        String action = adminAction.value();
        Object[] args = joinPoint.getArgs();

        log.info("[ADMIN_ACTION] adminId: {} - {} - params: {}",
                adminId, action, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            log.info("[ADMIN_ACTION] adminId: {} - {} - SUCCESS", adminId, action);
            return result;
        } catch (Exception e) {
            log.error("[ADMIN_ACTION] adminId: {} - {} - FAILED",
                    adminId, action, e);
            throw e;
        }
    }
}
