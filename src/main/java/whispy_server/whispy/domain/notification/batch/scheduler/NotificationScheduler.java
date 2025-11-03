package whispy_server.whispy.domain.notification.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final JobLauncher jobLauncher;

    @Qualifier("deleteOldNotificationJob")
    private final Job deleteOldNotificationJob;

    @Scheduled(cron = "0 0 3 * * *")
    public void deleteOldNotifications () {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLocalDateTime("executeAt", LocalDateTime.now())
                    .toJobParameters();

            jobLauncher.run(deleteOldNotificationJob, jobParameters);
        } catch (Exception e) {
            throw BatchJobExecutionFailedException.EXCEPTION;
        }
    }
}
