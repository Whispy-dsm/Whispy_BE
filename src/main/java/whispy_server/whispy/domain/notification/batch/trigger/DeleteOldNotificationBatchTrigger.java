package whispy_server.whispy.domain.notification.batch.trigger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.time.LocalDateTime;

/**
 * 오래된 알림 삭제 배치 작업 트리거.
 *
 * Spring Batch Job을 실행하기 위한 전용 컴포넌트입니다.
 * 트랜잭션 없이 동작하여 JobLauncher가 자체 트랜잭션을 관리할 수 있도록 합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteOldNotificationBatchTrigger {

    private final JobLauncher jobLauncher;

    @Qualifier("deleteOldNotificationJob")
    private final Job deleteOldNotificationJob;

    /**
     * 오래된 알림을 삭제하는 배치 작업을 트리거합니다.
     *
     * @throws BatchJobExecutionFailedException 배치 작업 실행 실패 시
     */
    public void trigger() {
        log.info("오래된 알림 삭제 배치 작업 트리거 시작");

        try {
            log.debug("배치 JobParameters 생성 시작");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLocalDateTime("executeAt", LocalDateTime.now())
                    .toJobParameters();
            log.debug("배치 JobParameters 생성 완료");

            log.info("배치 작업 실행 시작");
            jobLauncher.run(deleteOldNotificationJob, jobParameters);
            log.info("배치 작업 실행 완료");

        } catch (Exception e) {
            log.error("배치 작업 실행 실패 - error: {}", e.getMessage(), e);
            throw new BatchJobExecutionFailedException(e);
        }
    }
}
