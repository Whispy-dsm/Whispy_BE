package whispy_server.whispy.domain.topic.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.batch.dto.AddTopicJobParameters;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddTopicItemProcessor implements ItemProcessor<UserJpaEntity, AddTopicJobParameters> {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;

    private NotificationTopic newTopic;
    private boolean defaultSubscribed;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        this.newTopic = NotificationTopic.valueOf(jobParameters.getString("newTopic"));
        this. defaultSubscribed = Boolean.parseBoolean(jobParameters.getString("defaultSubscribed"));
    }

    @Override
    public AddTopicJobParameters process(UserJpaEntity user) throws Exception {
        boolean alreadyExists = queryTopicSubscriptionPort
                .findByEmailAndTopic(user.getEmail(), newTopic)
                .isPresent();

        if(alreadyExists){
            log.debug("사용자 {}는 이미 토픽 {}을 가지고 있음", user.getEmail(), newTopic);
            return null;
        }

        return new AddTopicJobParameters(
                user.getEmail(),
                user.getFcmToken(),
                newTopic,
                defaultSubscribed
        );
    }

}
