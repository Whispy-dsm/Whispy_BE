package whispy_server.whispy.global.exception;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

/**
 * 예외 발생 시 Sentry 및 Discord로 알림을 전송하는 헬퍼.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorNotificationHandler {

    private final DiscordNotificationService discordNotificationService;

    /**
      * 도메인 예외를 상태 코드별로 분기해 모니터링 시스템에 전파한다.
      */
    public void handleWhispyException(WhispyException e) {

        int statusCode = e.getErrorCode().getStatusCode();

        if (statusCode == 400 || (statusCode >= 500 && statusCode < 600)) {
            log.error("[Whispy] 심각한 에러 발생 (400/5xx) - Status: {}, ErrorCode: {}, Message: {}",
                statusCode, e.getErrorCode(), e.getErrorCode().getMessage(), e);
            Sentry.captureException(e);
            discordNotificationService.sendErrorNotification(e);
        } else {
            log.warn("[Whispy] 일반적인 클라이언트 에러 발생 (4xx) - Status: {}, ErrorCode: {}, Message: {}",
                statusCode, e.getErrorCode(), e.getErrorCode().getMessage(), e);
        }
    }

    /**
      * 일반 예외를 모니터링 시스템에 전파한다.
      */
    public void handleExceptionException(Exception e) {
        log.error("예상치 못한 예외 발생 - ExceptionType: {}, Message: {}",
            e.getClass().getSimpleName(), e.getMessage(), e);
        Sentry.captureException(e);
        discordNotificationService.sendErrorNotification(e);
    }
}
