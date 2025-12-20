package whispy_server.whispy.domain.notification.batch.trigger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.util.Collections;

/**
 * 알림 저장 배치 작업 트리거.
 *
 * Spring Batch Job을 실행하기 위한 전용 컴포넌트입니다.
 * 트랜잭션 없이 동작하여 JobLauncher가 자체 트랜잭션을 관리할 수 있도록 합니다.
 */
@Component
@RequiredArgsConstructor
public class SaveNotificationBatchTrigger {

    private final JobLauncher jobLauncher;

    @Qualifier("saveNotificationJob")
    private final Job saveNotificationJob;

    private final ObjectMapper objectMapper;

    /**
     * 알림 저장 배치 작업을 트리거합니다.
     *
     * @param request 알림 토픽 전송 요청
     * @throws BatchJobExecutionFailedException 배치 작업 실행 실패 시
     */
    public void trigger(NotificationTopicSendRequest request) {

        try {
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
            throw new BatchJobExecutionFailedException(e);
        }
    }
}
