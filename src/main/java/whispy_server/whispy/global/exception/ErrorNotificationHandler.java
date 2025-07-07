package whispy_server.whispy.global.exception;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorNotificationHandler {

    private final DiscordNotificationService discordNotificationService;

    public void handleWhispyException(WhispyException e) {

        int statusCode = e.getErrorCode().getStatusCode();

        if (statusCode == 400 || (statusCode >= 500 && statusCode < 600)) {
            Sentry.captureException(e);
            discordNotificationService.sendErrorNotification(e);
        } else {
            log.error(e.toString());
        }
    }

    public void handleExceptionException(Exception e) {
        Sentry.captureException(e);
        discordNotificationService.sendErrorNotification(e);
    }
}