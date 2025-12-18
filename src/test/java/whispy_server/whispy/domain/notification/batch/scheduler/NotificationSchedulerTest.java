package whispy_server.whispy.domain.notification.batch.scheduler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * NotificationScheduler의 단위 테스트 클래스
 * <p>
 * 알림 배치 스케줄러의 다양한 시나리오를 검증합니다.
 * 배치 작업 실행 성공, 실패 및 Sentry/Discord 연동을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationScheduler 테스트")
class NotificationSchedulerTest {

    @InjectMocks
    private NotificationScheduler scheduler;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job deleteOldNotificationJob;

    @Mock
    private DiscordNotificationService discordNotificationService;

    /**
     * 성공적인 배치 실행 테스트용 JobExecution을 생성합니다.
     *
     * @return COMPLETED 상태의 JobExecution 객체
     */
    private JobExecution createSuccessfulJobExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("executeAt", LocalDateTime.now())
                .toJobParameters();

        JobExecution jobExecution = new JobExecution(
                new JobInstance(1L, "deleteOldNotificationJob"),
                jobParameters
        );
        jobExecution.setStatus(BatchStatus.COMPLETED);
        jobExecution.setEndTime(LocalDateTime.now());

        return jobExecution;
    }

    @Test
    @DisplayName("배치 작업이 성공적으로 실행된다")
    void deleteOldNotifications_executesSuccessfully() throws Exception {
        // given
        JobExecution successfulExecution = createSuccessfulJobExecution();
        given(jobLauncher.run(eq(deleteOldNotificationJob), any(JobParameters.class)))
                .willReturn(successfulExecution);

        // when
        scheduler.deleteOldNotifications();

        // then
        verify(jobLauncher).run(eq(deleteOldNotificationJob), any(JobParameters.class));
        verify(discordNotificationService, never()).sendErrorNotification(any());
    }

    @Test
    @DisplayName("배치 작업 실행 시 올바른 JobParameters를 전달한다")
    void deleteOldNotifications_passesCorrectJobParameters() throws Exception {
        // given
        JobExecution successfulExecution = createSuccessfulJobExecution();
        given(jobLauncher.run(eq(deleteOldNotificationJob), any(JobParameters.class)))
                .willReturn(successfulExecution);

        ArgumentCaptor<JobParameters> jobParamsCaptor = ArgumentCaptor.forClass(JobParameters.class);

        // when
        scheduler.deleteOldNotifications();

        // then
        verify(jobLauncher).run(eq(deleteOldNotificationJob), jobParamsCaptor.capture());

        JobParameters capturedParams = jobParamsCaptor.getValue();
        assertThat(capturedParams.getParameters()).containsKey("executeAt");
    }

    @Test
    @DisplayName("배치 작업 실패 시 Discord 알림을 전송한다")
    void deleteOldNotifications_sendsDiscordNotification_whenJobFails() throws Exception {
        // given
        Exception expectedException = new RuntimeException("배치 작업 실패");
        given(jobLauncher.run(eq(deleteOldNotificationJob), any(JobParameters.class)))
                .willThrow(expectedException);

        // when
        scheduler.deleteOldNotifications();

        // then
        verify(discordNotificationService).sendErrorNotification(any());
    }

    @Test
    @DisplayName("배치 작업 실패 시 예외를 던지지 않는다")
    void deleteOldNotifications_doesNotThrowException_whenJobFails() throws Exception {
        // given
        given(jobLauncher.run(eq(deleteOldNotificationJob), any(JobParameters.class)))
                .willThrow(new RuntimeException("배치 작업 실패"));

        // when & then - 예외가 발생하지 않아야 함
        scheduler.deleteOldNotifications();

        // 스케줄러가 계속 실행될 수 있도록 예외를 삼키는지 확인
        verify(jobLauncher).run(eq(deleteOldNotificationJob), any(JobParameters.class));
    }

    @Test
    @DisplayName("JobLauncher가 null을 반환해도 정상 처리된다")
    void deleteOldNotifications_handlesNullJobExecution() throws Exception {
        // given
        given(jobLauncher.run(eq(deleteOldNotificationJob), any(JobParameters.class)))
                .willReturn(null);

        // when & then - NPE가 발생하지 않아야 함
        scheduler.deleteOldNotifications();

        verify(jobLauncher).run(eq(deleteOldNotificationJob), any(JobParameters.class));
    }

    @Test
    @DisplayName("배치 작업이 FAILED 상태로 완료되어도 Discord 알림을 보내지 않는다")
    void deleteOldNotifications_doesNotSendNotification_whenJobCompletesWithFailedStatus() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("executeAt", LocalDateTime.now())
                .toJobParameters();

        JobExecution failedExecution = new JobExecution(
                new JobInstance(1L, "deleteOldNotificationJob"),
                jobParameters
        );
        failedExecution.setStatus(BatchStatus.FAILED);
        failedExecution.setEndTime(LocalDateTime.now());

        given(jobLauncher.run(eq(deleteOldNotificationJob), any(JobParameters.class)))
                .willReturn(failedExecution);

        // when
        scheduler.deleteOldNotifications();

        // then
        // 배치가 실행은 됐지만 FAILED 상태 - 예외는 안 던져짐
        verify(discordNotificationService, never()).sendErrorNotification(any());
    }
}
