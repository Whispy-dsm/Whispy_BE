package whispy_server.whispy.domain.notification.application.service.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.notification.application.service.SendToTopicBatchService;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * SendToTopicBatchService의 단위 테스트 클래스
 * <p>
 * 토픽으로 알림 전송 배치 서비스를 검증합니다.
 * FCM 전송 및 배치 작업 실행 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SendToTopicBatchService 테스트")
class SendToTopicBatchServiceTest {

    @InjectMocks
    private SendToTopicBatchService service;

    @Mock
    private FcmSendPort fcmSendPort;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job saveNotificationJob;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("토픽으로 알림을 전송할 수 있다")
    void execute_sendsNotificationToTopic() throws Exception {
        // given
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");

        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "테스트 제목",
                "테스트 내용",
                data
        );

        given(objectMapper.writeValueAsString(data)).willReturn("{\"key\":\"value\"}");

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
    void execute_launchesBatchJob() throws Exception {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.SYSTEM_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        given(objectMapper.writeValueAsString(any())).willReturn("{}");

        // when
        service.execute(request);

        // then
        verify(jobLauncher).run(eq(saveNotificationJob), any(JobParameters.class));
    }

    @Test
    @DisplayName("데이터 없이 알림을 전송할 수 있다")
    void execute_sendsNotificationWithoutData() throws Exception {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        given(objectMapper.writeValueAsString(any())).willReturn("{}");

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
    void execute_throwsException_whenBatchJobFails() throws Exception {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        given(objectMapper.writeValueAsString(any())).willReturn("{}");
        given(jobLauncher.run(any(Job.class), any(JobParameters.class)))
                .willThrow(new RuntimeException("Batch job failed"));

        // when & then
        assertThrows(BatchJobExecutionFailedException.class,
                () -> service.execute(request));
    }

    @Test
    @DisplayName("JSON 변환 실패 시 BatchJobExecutionFailedException이 발생한다")
    void execute_throwsException_whenJsonConversionFails() throws Exception {
        // given
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");

        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                data
        );

        given(objectMapper.writeValueAsString(data))
                .willThrow(new RuntimeException("JSON conversion failed"));

        // when & then
        assertThrows(BatchJobExecutionFailedException.class,
                () -> service.execute(request));
    }

    @Test
    @DisplayName("다양한 토픽으로 알림을 전송할 수 있다")
    void execute_sendsToVariousTopics() throws Exception {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.ONLY_ADMIN,
                "관리자 공지",
                "관리자 전용 공지사항입니다",
                null
        );

        given(objectMapper.writeValueAsString(any())).willReturn("{}");

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
