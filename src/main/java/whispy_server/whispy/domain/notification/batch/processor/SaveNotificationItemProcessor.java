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

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveNotificationItemProcessor implements ItemProcessor<TopicSubscriptionJpaEntity, NotificationJobParameters> {

    private final ObjectMapper objectMapper;

    private NotificationTopic topic;
    private String title;
    private String body;
    private Map<String, String> data;

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
