package whispy_server.whispy.domain.notification.batch.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.batch.dto.NotificationJobParameters;
import whispy_server.whispy.domain.topic.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.Collections;
import java.util.Map;

/**
 * 알림 저장 Item Processor.
 *
 * TopicSubscriptionJpaEntity를 NotificationJobParameters로 변환하는 배치 프로세서입니다.
 * Job 파라미터에서 알림 정보를 읽어와 각 구독자에 대한 알림 파라미터를 생성합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SaveNotificationItemProcessor implements ItemProcessor<TopicSubscriptionJpaEntity, NotificationJobParameters> {

    private final ObjectMapper objectMapper;

    private NotificationTopic topic;
    private String title;
    private String body;
    private Map<String, String> data;

    /**
     * Step 시작 전에 Job 파라미터에서 알림 정보를 초기화합니다.
     *
     * @param stepExecution Step 실행 정보
     */
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        this.topic = NotificationTopic.valueOf(jobParameters.getString("topic"));
        this.title = jobParameters.getString("title");
        this.body = jobParameters.getString("body");

        try {
            String dataJson = jobParameters.getString("data");
            this.data = objectMapper.readValue(dataJson, new TypeReference<Map<String, String>>() {});

        } catch (Exception e) {
            log.warn("데이터 파싱 실패, 빈 맵 사용: {}", e.getMessage());
            this.data = Collections.emptyMap();
        }
    }

    /**
     * 토픽 구독 엔티티를 알림 파라미터로 변환합니다.
     *
     * @param subscription 토픽 구독 엔티티
     * @return 알림 저장 파라미터
     * @throws Exception 변환 중 예외 발생 시
     */
    @Override
    public NotificationJobParameters process(TopicSubscriptionJpaEntity subscription) throws Exception {
        return new NotificationJobParameters(
                subscription.getEmail(),
                title,
                body,
                topic,
                data
        );
    }
}
