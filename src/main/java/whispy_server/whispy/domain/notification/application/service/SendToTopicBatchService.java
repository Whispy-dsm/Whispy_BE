package whispy_server.whispy.domain.notification.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.util.Collections;

/**
 * 토픽으로 알림 전송 배치 서비스.
 *
 * FCM 토픽을 통해 알림을 전송하고 Spring Batch를 사용하여 알림 이력을 저장하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class SendToTopicBatchService implements SendToTopicUseCase {

    private final FcmSendPort fcmSendPort;
    private final JobLauncher jobLauncher;

    @Qualifier("saveNotificationJob")
    private final Job saveNotificationJob;

    private final ObjectMapper objectMapper;

    /**
     * 토픽으로 알림을 전송하고 배치 작업을 통해 알림 이력을 저장합니다.
     *
     * @param request 알림 토픽 전송 요청
     * @throws whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException 배치 작업 실행 실패 시
     */
    @Override
    public void execute(NotificationTopicSendRequest request) {
        try {

            fcmSendPort.sendToTopic(
                    request.topic(),
                    request.title(),
                    request.body(),
                    request.data()
            );

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("topic", request.topic().name())
                    .addString("title", request.title())
                    .addString("body", request.body())
                    .addString("data", request.data() != null ?
                            objectMapper.writeValueAsString(request.data()) :
                            objectMapper.writeValueAsString(Collections.emptyMap()))
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(saveNotificationJob, jobParameters);

        } catch (Exception e) {
            throw BatchJobExecutionFailedException.EXCEPTION;
        }
    }
}
