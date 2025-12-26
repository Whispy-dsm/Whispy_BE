package whispy_server.whispy.domain.notification.batch.scheduler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.batch.trigger.DeleteOldNotificationBatchTrigger;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

/**
 * NotificationScheduler의 단위 테스트 클래스
 *
 * 알림 배치 스케줄러의 다양한 시나리오를 검증합니다.
 * 배치 작업 실행 성공, 실패 및 Sentry/Discord 연동을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationScheduler 테스트")
class NotificationSchedulerTest {

    @InjectMocks
    private NotificationScheduler scheduler;

    @Mock
    private DeleteOldNotificationBatchTrigger deleteOldNotificationBatchTrigger;

    @Mock
    private DiscordNotificationService discordNotificationService;

    @Test
    @DisplayName("배치 작업이 성공적으로 실행된다")
    void deleteOldNotifications_executesSuccessfully() {
        // when
        scheduler.deleteOldNotifications();

        // then
        verify(deleteOldNotificationBatchTrigger).trigger();
        verify(discordNotificationService, never()).sendErrorNotification(any());
    }

    @Test
    @DisplayName("배치 트리거를 호출한다")
    void deleteOldNotifications_callsTrigger() {
        // when
        scheduler.deleteOldNotifications();

        // then
        verify(deleteOldNotificationBatchTrigger).trigger();
    }

    @Test
    @DisplayName("배치 작업 실패 시 Discord 알림을 전송한다")
    void deleteOldNotifications_sendsDiscordNotification_whenJobFails() {
        // given
        Exception expectedException = new BatchJobExecutionFailedException(new RuntimeException("배치 작업 실패"));
        willThrow(expectedException)
                .given(deleteOldNotificationBatchTrigger).trigger();

        // when
        scheduler.deleteOldNotifications();

        // then
        verify(discordNotificationService).sendErrorNotification(any());
    }

    @Test
    @DisplayName("배치 작업 실패 시 예외를 던지지 않는다")
    void deleteOldNotifications_doesNotThrowException_whenJobFails() {
        // given
        willThrow(new BatchJobExecutionFailedException(new RuntimeException("배치 작업 실패")))
                .given(deleteOldNotificationBatchTrigger).trigger();

        // when & then - 예외가 발생하지 않아야 함
        scheduler.deleteOldNotifications();

        // 스케줄러가 계속 실행될 수 있도록 예외를 삼키는지 확인
        verify(deleteOldNotificationBatchTrigger).trigger();
    }

    @Test
    @DisplayName("배치 트리거가 정상 완료되면 Discord 알림을 보내지 않는다")
    void deleteOldNotifications_doesNotSendNotification_whenTriggerSucceeds() {
        // when
        scheduler.deleteOldNotifications();

        // then
        verify(deleteOldNotificationBatchTrigger).trigger();
        verify(discordNotificationService, never()).sendErrorNotification(any());
    }
}
