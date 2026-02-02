package whispy_server.whispy.global.exception;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

/**
 * 예외 발생 시 Sentry 및 Discord로 알림을 전송하는 헬퍼.
 *
 * 로컬 환경에서는 외부 모니터링 서비스로 전송하지 않고 로그만 기록한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorNotificationHandler {

    private final DiscordNotificationService discordNotificationService;

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    /**
      * 도메인 예외를 상태 코드별로 분기해 모니터링 시스템에 전파한다.
      */
    public void handleWhispyException(WhispyException e) {

        int statusCode = e.getErrorCode().getStatusCode();

        if (statusCode == 400 || (statusCode >= 500 && statusCode < 600)) {
            log.error("[Whispy] 심각한 에러 발생 (400/5xx) - Status: {}, ErrorCode: {}, Message: {}",
                statusCode, e.getErrorCode(), e.getErrorCode().getMessage(), e);

            if (isProductionEnvironment()) {
                Sentry.captureException(e);
                discordNotificationService.sendErrorNotification(e);
                discordNotificationService.sendLogNotification(
                        "ERROR",
                        e.getErrorCode().getMessage(),
                        e.getErrorCode().name(),
                        e
                );
            } else {
                log.debug("[로컬 환경] Sentry/Discord 전송 생략 - Profile: {}", activeProfile);
            }
        } else {
            log.warn("[Whispy] 일반적인 클라이언트 에러 발생 (4xx) - Status: {}, ErrorCode: {}, Message: {}",
                statusCode, e.getErrorCode(), e.getErrorCode().getMessage());
        }
    }

    /**
      * 일반 예외를 모니터링 시스템에 전파한다.
      */
    public void handleExceptionException(Exception e) {
        log.error("예상치 못한 예외 발생 - ExceptionType: {}, Message: {}",
            e.getClass().getSimpleName(), e.getMessage(), e);

        if (isProductionEnvironment()) {
            Sentry.captureException(e);
            discordNotificationService.sendErrorNotification(e);
            discordNotificationService.sendLogNotification(
                    "ERROR",
                    e.getMessage() != null ? e.getMessage() : "메시지 없음",
                    e.getClass().getSimpleName(),
                    e
            );
        } else {
            log.debug("[로컬 환경] Sentry/Discord 전송 생략 - Profile: {}", activeProfile);
        }
    }

    /**
     * 운영 환경 여부를 확인합니다.
     *
     * 운영 환경(prod, stag)에서만 Sentry와 Discord로 알림을 전송하고,
     * 로컬 환경에서는 로그만 기록합니다.
     *
     * @return 운영 환경(prod 또는 stag)이면 true, 그 외(local 등) false
     */
    private boolean isProductionEnvironment() {
        return "prod".equals(activeProfile) ||
               "stag".equals(activeProfile);
    }
}
