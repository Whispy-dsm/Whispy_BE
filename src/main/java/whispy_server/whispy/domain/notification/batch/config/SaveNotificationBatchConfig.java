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
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import whispy_server.whispy.domain.topic.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.notification.batch.dto.NotificationJobParameters;
import whispy_server.whispy.domain.notification.batch.processor.SaveNotificationItemProcessor;
import whispy_server.whispy.domain.notification.batch.writer.SaveNotificationItemWriter;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.configuration.annotation.StepScope;

import java.util.Map;

/**
 * 알림 저장 배치 설정.
 *
 * FCM 토픽을 구독한 사용자들에게 알림 이력을 저장하는 Spring Batch 작업을 설정합니다.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SaveNotificationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final SaveNotificationItemWriter saveNotificationItemWriter;
    private final SaveNotificationItemProcessor saveNotificationItemProcessor;

    private static final int CHUNK_SIZE = 1000;

    /**
     * 알림 저장 Job을 생성합니다.
     *
     * @param saveNotificationBatchStep 알림 저장 Step
     * @return 알림 저장 Job
     */
    @Bean("saveNotificationJob")
    public Job saveNotificationBatchJob(
            @Qualifier("saveNotificationStep") Step saveNotificationBatchStep) {
        return new JobBuilder("saveNotificationJob", jobRepository)
                .start(saveNotificationBatchStep)
                .build();
    }

    /**
     * 알림 저장 Step을 생성합니다.
     *
     * @param topicSubscriberItemReader 토픽 구독자 Reader
     * @return 알림 저장 Step
     */
    @Bean("saveNotificationStep")
    public Step saveNotificationBatchStep(
            ItemReader<TopicSubscriptionJpaEntity> topicSubscriberItemReader) {
        return new StepBuilder("saveNotificationStep", jobRepository)
                .<TopicSubscriptionJpaEntity, NotificationJobParameters>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(topicSubscriberItemReader)
                .processor(saveNotificationItemProcessor)
                .writer(saveNotificationItemWriter)
                .build();
    }

    /**
     * 특정 토픽을 구독한 사용자를 조회하는 Reader를 생성합니다.
     *
     * @param topicParam 토픽 파라미터
     * @param entityManagerFactory EntityManagerFactory
     * @return 토픽 구독자 Reader
     * @throws Exception Reader 초기화 실패 시
     */
    @Bean
    @StepScope
    public JpaPagingItemReader<TopicSubscriptionJpaEntity> topicSubscriberItemReader(
            @Value("#{jobParameters['topic']}") String topicParam,
            EntityManagerFactory entityManagerFactory
    ) throws Exception {
        NotificationTopic topicEnum = NotificationTopic.valueOf(topicParam);

        JpaPagingItemReader<TopicSubscriptionJpaEntity> reader = new JpaPagingItemReader<>();
        reader.setQueryString("SELECT ts FROM TopicSubscriptionJpaEntity ts WHERE ts.topic = :topic AND ts.subscribed = true ORDER BY ts.id");
        reader.setParameterValues(Map.of("topic", topicEnum));
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(CHUNK_SIZE);
        reader.afterPropertiesSet();
        return reader;
    }
}
