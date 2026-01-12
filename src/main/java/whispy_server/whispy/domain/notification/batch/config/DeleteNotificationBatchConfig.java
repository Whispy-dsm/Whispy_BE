package whispy_server.whispy.domain.notification.batch.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;
import whispy_server.whispy.domain.notification.batch.dto.DeleteOldNotificationJobParameters;
import whispy_server.whispy.domain.notification.batch.processor.DeleteOldNotificationItemProcessor;
import whispy_server.whispy.domain.notification.batch.reader.ZeroOffsetJpaPagingItemReader;
import whispy_server.whispy.domain.notification.batch.writer.DeleteOldNotificationItemWriter;
import whispy_server.whispy.global.exception.domain.batch.BatchItemReaderInitializationFailedException;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 오래된 알림 삭제 배치 설정.
 *
 * 30일이 지난 오래된 알림을 삭제하는 Spring Batch 작업을 설정합니다.
 */
@Configuration
@RequiredArgsConstructor
public class DeleteNotificationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final DeleteOldNotificationItemWriter deleteOldNotificationItemWriter;
    private final DeleteOldNotificationItemProcessor deleteOldNotificationItemProcessor;

    private static final int CHUNK_SIZE = 1000;

    /**
     * 오래된 알림 삭제 Job을 생성합니다.
     *
     * @param deleteOldNotificationStep 알림 삭제 Step
     * @return 알림 삭제 Job
     */
    @Bean("deleteOldNotificationJob")
    public Job deleteOldNotificationJob(
            @Qualifier("deleteOldNotificationStep") Step deleteOldNotificationStep) {
        return new JobBuilder("deleteOldNotificationJob", jobRepository)
                .start(deleteOldNotificationStep)
                .build();
    }

    /**
     * 오래된 알림 삭제 Step을 생성합니다.
     *
     * @param oldNotificationReader 오래된 알림 Reader
     * @return 알림 삭제 Step
     */
    @Bean("deleteOldNotificationStep")
    public Step deleteOldNotificationStep(
            JpaPagingItemReader<NotificationJpaEntity> oldNotificationReader) {
        return new StepBuilder("deleteOldNotificationStep", jobRepository)
                .<NotificationJpaEntity, DeleteOldNotificationJobParameters>chunk(CHUNK_SIZE,
                        platformTransactionManager)
                .reader(oldNotificationReader)
                .processor(deleteOldNotificationItemProcessor)
                .writer(deleteOldNotificationItemWriter)
                .build();
    }

    /**
     * 30일이 지난 오래된 알림을 조회하는 Reader를 생성합니다.
     *
     * ZeroOffsetJpaPagingItemReader를 사용하여 항상 0페이지(OFFSET 0)에서만 읽습니다.
     * 이렇게 하면 삭제된 데이터 수만큼 앞으로 당겨진 데이터도 놓치지 않고 처리할 수 있습니다.
     *
     * @return 오래된 알림 Reader
     */
    @Bean
    @StepScope
    public JpaPagingItemReader<NotificationJpaEntity> oldNotificationReader(
            EntityManagerFactory entityManagerFactory) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        try {
            ZeroOffsetJpaPagingItemReader<NotificationJpaEntity> reader = new ZeroOffsetJpaPagingItemReader<>();
            reader.setName("oldNotificationReader");
            reader.setEntityManagerFactory(entityManagerFactory);
            reader.setQueryString(
                    "SELECT n FROM NotificationJpaEntity n WHERE n.createdAt < :thirtyDaysAgo ORDER BY n.id ASC");
            reader.setParameterValues(Map.of("thirtyDaysAgo", thirtyDaysAgo));
            reader.setPageSize(CHUNK_SIZE);
            return reader;
        } catch (Exception e) {
            throw new BatchItemReaderInitializationFailedException(e);
        }
    }
}
