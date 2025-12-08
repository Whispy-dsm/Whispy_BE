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

/**
 * 토픽 추가 배치 아이템 프로세서.
 *
 * 사용자 엔티티를 토픽 추가 작업 파라미터로 변환하는 프로세서입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AddTopicItemProcessor implements ItemProcessor<UserJpaEntity, AddTopicJobParameters> {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;

    private NotificationTopic newTopic;
    private boolean defaultSubscribed;

    /**
     * Step 실행 전에 Job 파라미터를 초기화합니다.
     *
     * @param stepExecution Step 실행 정보
     */
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        this.newTopic = NotificationTopic.valueOf(jobParameters.getString("newTopic"));
        this. defaultSubscribed = Boolean.parseBoolean(jobParameters.getString("defaultSubscribed"));
    }

    /**
     * 사용자 엔티티를 토픽 추가 작업 파라미터로 변환합니다.
     *
     * @param user 사용자 엔티티
     * @return 토픽 추가 작업 파라미터 (이미 존재하는 경우 null)
     * @throws Exception 처리 중 예외 발생 시
     */
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
