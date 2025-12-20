package whispy_server.whispy.domain.topic.batch.trigger;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

/**
 * 새로운 토픽 추가 배치 작업 트리거.
 *
 * Spring Batch Job을 실행하기 위한 전용 컴포넌트입니다.
 * 트랜잭션 없이 동작하여 JobLauncher가 자체 트랜잭션을 관리할 수 있도록 합니다.
 */
@Component
@RequiredArgsConstructor
public class AddNewTopicBatchTrigger {

    private final JobLauncher jobLauncher;

    @Qualifier("addNewTopicJob")
    private final Job addNewTopicJob;

    /**
     * 모든 사용자에게 새로운 토픽을 추가하는 배치 작업을 트리거합니다.
     *
     * @param newTopic 새로운 토픽
     * @param defaultSubscribed 기본 구독 여부
     * @throws BatchJobExecutionFailedException 배치 작업 실행 실패 시
     */
    public void trigger(NotificationTopic newTopic, boolean defaultSubscribed) {

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("newTopic", newTopic.name())
                    .addString("defaultSubscribed", String.valueOf(defaultSubscribed))
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(addNewTopicJob, jobParameters);

        } catch (Exception e) {
            throw new BatchJobExecutionFailedException(e);
        }
    }
}
