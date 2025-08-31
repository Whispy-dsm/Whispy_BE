package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.topic.application.port.in.AddNewTopicForAllUsersUseCase;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddNewTopicBatchService implements AddNewTopicForAllUsersUseCase {

    private final JobLauncher jobLauncher;

    @Qualifier("addNewTopicJob")
    private final Job addNewTopicJob;

    @Override
    public void execute(NotificationTopic newTopic, boolean defaultSubscribed) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("newTopic", newTopic.name())
                    .addString("defaultSubscribed", String.valueOf(defaultSubscribed))
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            log.info("새 토픽 {} 배치 작업 시작 (기본구독: {})", newTopic, defaultSubscribed);

            jobLauncher.run(addNewTopicJob, jobParameters);

            log.info("새 토픽 {} 배치 작업 완료", newTopic);

        } catch (Exception e) {
            log.error("새 토픽 {} 배치 작업 실패: {}", newTopic, e.getMessage(), e);
            throw new RuntimeException("배치 작업 실패", e);
        }
    }
}
