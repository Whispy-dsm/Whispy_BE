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
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

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

            jobLauncher.run(addNewTopicJob, jobParameters);

        } catch (Εxception e) {
            throw BatchJobExecutionFailedΕxception.EXCEPTION;
        }
    }
}
