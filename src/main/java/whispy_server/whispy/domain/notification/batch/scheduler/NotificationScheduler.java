package whispy_server.whispy.domain.notification.batch.scheduler;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

import java.time.LocalDateTime;

/**
 * 알림 배치 스케줄러.
 *
 * 오래된 알림을 주기적으로 삭제하는 배치 작업을 스케줄링합니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final JobLauncher jobLauncher;
    private final DiscordNotificationService discordNotificationService;

    @Qualifier("deleteOldNotificationJob")
    private final Job deleteOldNotificationJob;

    /**
     * 매일 새벽 3시에 30일이 지난 오래된 알림을 삭제합니다.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteOldNotifications() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLocalDateTime("executeAt", LocalDateTime.now())
                    .toJobParameters();

            jobLauncher.run(deleteOldNotificationJob, jobParameters);

        } catch (Exception e) {

            // Sentry로 예외 전송
            Sentry.captureException(e);

            // Discord로 알림 전송
            BatchJobExecutionFailedException batchException = new BatchJobExecutionFailedException();
            discordNotificationService.sendErrorNotification(batchException);
        }
    }
}
