package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.topic.application.port.in.AddNewTopicForAllUsersUseCase;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

/**
 * 새로운 토픽 추가 배치 서비스.
 *
 * Spring Batch를 사용하여 모든 사용자에게 새로운 토픽을 추가하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class AddNewTopicBatchService implements AddNewTopicForAllUsersUseCase {

    private final JobLauncher jobLauncher;

    @Qualifier("addNewTopicJob")
    private final Job addNewTopicJob;

    /**
     * 모든 사용자에게 새로운 토픽을 추가합니다.
     *
     * @param newTopic 새로운 토픽
     * @param defaultSubscribed 기본 구독 여부
     * @throws BatchJobExecutionFailedException 배치 작업 실행 실패 시
     */
    @Override
    public void execute(NotificationTopic newTopic, boolean defaultSubscribed) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("newTopic", newTopic.name())
                    .addString("defaultSubscribed", String.valueOf(defaultSubscribed))
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(addNewTopicJob, jobParameters);

        } catch (Exception e) {
            throw BatchJobExecutionFailedException.EXCEPTION;
        }
    }
}
