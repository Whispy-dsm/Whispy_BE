package whispy_server.whispy.domain.notification.batch.scheduler;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.batch.trigger.DeleteOldNotificationBatchTrigger;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

/**
 * 알림 배치 스케줄러.
 *
 * 오래된 알림을 주기적으로 삭제하는 배치 작업을 스케줄링합니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final DeleteOldNotificationBatchTrigger deleteOldNotificationBatchTrigger;
    private final DiscordNotificationService discordNotificationService;

    /**
     * 매일 새벽 3시에 30일이 지난 오래된 알림을 삭제합니다.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteOldNotifications() {

        try {
            deleteOldNotificationBatchTrigger.trigger();
        } catch (Exception e) {
            Sentry.captureException(e);
            discordNotificationService.sendErrorNotification(e);
        }
    }
}
