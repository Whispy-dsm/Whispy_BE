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
            Sentry.captureException(e);
            discordNotificationService.sendErrorNotification(e);
        } else {
            log.error(e.toString());
        }
    }

    /**
      * 일반 예외를 모니터링 시스템에 전파한다.
      */
    public void handleExceptionException(Exception e) {
        Sentry.captureException(e);
        discordNotificationService.sendErrorNotification(e);
    }
}
