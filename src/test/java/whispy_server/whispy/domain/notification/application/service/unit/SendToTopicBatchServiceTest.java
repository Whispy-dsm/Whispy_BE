package whispy_server.whispy.domain.notification.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.notification.application.service.SendToTopicBatchService;
import whispy_server.whispy.domain.notification.batch.trigger.SaveNotificationBatchTrigger;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

/**
 * SendToTopicBatchService의 단위 테스트 클래스
 *
 * 토픽으로 알림 전송 배치 서비스를 검증합니다.
 * FCM 전송 및 배치 작업 실행 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SendToTopicBatchService 테스트")
class SendToTopicBatchServiceTest {

    @InjectMocks
    private SendToTopicBatchService service;

    @Mock
    private FcmSendPort fcmSendPort;

    @Mock
    private SaveNotificationBatchTrigger saveNotificationBatchTrigger;

    @Test
    @DisplayName("토픽으로 알림을 전송할 수 있다")
    void execute_sendsNotificationToTopic() {
        // given
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");

        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "테스트 제목",
                "테스트 내용",
                data
        );

        // when
        service.execute(request);

        // then
        verify(fcmSendPort).sendToTopic(
                eq(NotificationTopic.GENERAL_ANNOUNCEMENT),
                eq("테스트 제목"),
                eq("테스트 내용"),
                eq(data)
        );
    }

    @Test
    @DisplayName("알림 전송 후 배치 작업을 실행한다")
    void execute_launchesBatchJob() {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.SYSTEM_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        // when
        service.execute(request);

        // then
        verify(saveNotificationBatchTrigger).trigger(eq(request));
    }

    @Test
    @DisplayName("데이터 없이 알림을 전송할 수 있다")
    void execute_sendsNotificationWithoutData() {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        // when
        service.execute(request);

        // then
        verify(fcmSendPort).sendToTopic(
                eq(NotificationTopic.GENERAL_ANNOUNCEMENT),
                eq("제목"),
                eq("내용"),
                eq(null)
        );
    }

    @Test
    @DisplayName("배치 작업 실행 실패 시 BatchJobExecutionFailedException이 발생한다")
    void execute_throwsException_whenBatchJobFails() {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        willThrow(new BatchJobExecutionFailedException(new RuntimeException("Batch job failed")))
                .given(saveNotificationBatchTrigger).trigger(any(NotificationTopicSendRequest.class));

        // when & then
        assertThrows(BatchJobExecutionFailedException.class,
                () -> service.execute(request));
    }

    @Test
    @DisplayName("배치 트리거 실패 시 BatchJobExecutionFailedException이 발생한다")
    void execute_throwsException_whenBatchTriggerFails() {
        // given
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");

        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                data
        );

        willThrow(new BatchJobExecutionFailedException(new RuntimeException("Batch trigger failed")))
                .given(saveNotificationBatchTrigger).trigger(any(NotificationTopicSendRequest.class));

        // when & then
        assertThrows(BatchJobExecutionFailedException.class,
                () -> service.execute(request));
    }

    @Test
    @DisplayName("다양한 토픽으로 알림을 전송할 수 있다")
    void execute_sendsToVariousTopics() {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.ONLY_ADMIN,
                "관리자 공지",
                "관리자 전용 공지사항입니다",
                null
        );

        // when
        service.execute(request);

        // then
        verify(fcmSendPort).sendToTopic(
                eq(NotificationTopic.ONLY_ADMIN),
                eq("관리자 공지"),
                eq("관리자 전용 공지사항입니다"),
                eq(null)
        );
    }
}
