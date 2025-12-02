package whispy_server.whispy.domain.topic.batch.config;

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
import whispy_server.whispy.domain.topic.batch.dto.AddTopicJobParameters;
import whispy_server.whispy.domain.topic.batch.processor.AddTopicItemProcessor;
import whispy_server.whispy.domain.topic.batch.writer.AddTopicItemWriter;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;

/**
 * 새로운 토픽 추가 배치 설정.
 *
 * 모든 사용자에게 새로운 토픽을 추가하는 Spring Batch Job을 정의합니다.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AddNewTopicBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final AddTopicItemWriter addTopicItemWriter;
    private final AddTopicItemProcessor addTopicItemProcessor;

    private static final int CHUNK_SIZE = 1000;

    /**
     * 새로운 토픽 추가 배치 Job을 생성합니다.
     *
     * @return 새로운 토픽 추가 Job
     */
    @Bean("addNewTopicJob")
    public Job addNewTopicJob() {
        return new JobBuilder("addNewTopicJob", jobRepository)
                .start(addNewTopicStep())
                .build();
    }

    /**
     * 새로운 토픽 추가 배치 Step을 생성합니다.
     *
     * @return 새로운 토픽 추가 Step
     */
    @Bean("addNewTopicStep")
    public Step addNewTopicStep() {
        return new StepBuilder("addNewTopicStep", jobRepository)
                .<UserJpaEntity, AddTopicJobParameters>chunk(CHUNK_SIZE, transactionManager)
                .reader(userItemReader())
                .processor(addTopicItemProcessor)
                .writer(addTopicItemWriter)
                .build();
    }

    /**
     * 사용자 엔티티를 읽어오는 ItemReader를 생성합니다.
     *
     * @return 사용자 ItemReader
     */
    @Bean("userItemReader")
    public ItemReader<UserJpaEntity> userItemReader() {
        return new JpaPagingItemReaderBuilder<UserJpaEntity>()
                .name("userItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM UserJpaEntity u ORDER BY u.id")
                .pageSize(CHUNK_SIZE)
                .build();
    }

}
