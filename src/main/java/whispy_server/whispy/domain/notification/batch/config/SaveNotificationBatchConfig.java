package whispy_server.whispy.domain.notification.batch.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import whispy_server.whispy.domain.topic.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.notification.batch.dto.NotificationJobParameters;
import whispy_server.whispy.domain.notification.batch.processor.SaveNotificationItemProcessor;
import whispy_server.whispy.domain.notification.batch.writer.SaveNotificationItemWriter;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SaveNotificationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final SaveNotificationItemWriter saveNotificationItemWriter;
    private final SaveNotificationItemProcessor saveNotificationItemProcessor;

    private static final int CHUNK_SIZE = 1000;

    @Bean("saveNotificationJob")
    public Job saveNotificationBatchJob() {
        return new JobBuilder("saveNotificationJob", jobRepository)
                .start(saveNotificationBatchStep())
                .build();
    }

    @Bean("saveNotificationStep")
    public Step saveNotificationBatchStep() {
        return new StepBuilder("saveNotificationStep", jobRepository)
                .<TopicSubscriptionJpaEntity, NotificationJobParameters>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(topicSubscriberItemReader())
                .processor(saveNotificationItemProcessor)
                .writer(saveNotificationItemWriter)
                .build();
    }

    @Bean("topicSubscriberItemReader")
    public ItemReader<TopicSubscriptionJpaEntity> topicSubscriberItemReader() {
        return new JpaPagingItemReaderBuilder<TopicSubscriptionJpaEntity>()
                .name("topicSubscriberItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT ts FROM TopicSubscriptionJpaEntity ts WHERE ts.topic = :topic AND ts.isSubscribed = true ORDER BY ts.id")
                .parameterValues(Map.of("topic", "#{jobParameters['topic']}"))
                .pageSize(CHUNK_SIZE)
                .build();
    }
}
