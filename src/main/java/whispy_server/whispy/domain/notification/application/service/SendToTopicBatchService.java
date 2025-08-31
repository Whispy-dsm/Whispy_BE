package whispy_server.whispy.domain.notification.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SendToTopicBatchService implements SendToTopicUseCase {

    private final FcmSendPort fcmSendPort;
    private final JobLauncher jobLauncher;

    @Qualifier("saveNotificationJob")
    private final Job saveNotificationJob;

    private final ObjectMapper objectMapper;

    @Override
    public void execute(FcmSendRequest request) {
        try {

            fcmSendPort.sendToTopic(
                    request.topic(),
                    request.title(),
                    request.body(),
                    request.data()
            );

            log.info("FCM 토픽 발송 완료: {}", request.topic());


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
            log.info("알림 저장 배치 작업 시작: {}", request.topic());

        } catch (Exception e) {
            log.error("토픽 발송 처리 중 오류 발생: topic={}", request.topic(), e);
            throw new RuntimeException("토픽 발송 처리 실패", e);
        }
    }
}
