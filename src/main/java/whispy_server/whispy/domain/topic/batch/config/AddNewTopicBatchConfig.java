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

    @Bean("addNewTopicJob")
    public Job addNewTopicJob() {
        return new JobBuilder("addNewTopicJob", jobRepository)
                .start(addNewTopicStep())
                .build();
    }

    @Bean("addNewTopicStep")
    public Step addNewTopicStep() {
        return new StepBuilder("addNewTopicStep", jobRepository)
                .<UserJpaEntity, AddTopicJobParameters>chunk(CHUNK_SIZE, transactionManager)
                .reader(userItemReader())
                .processor(addTopicItemProcessor)
                .writer(addTopicItemWriter)
                .build();
    }

    @Bean("userItemReader")
    public ItemReader<UserJpaEntity> userItemReader() {
        return new JpaPagingItemReaderBuilder<UserJpaEntity>()
                .name("userItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM UserJpaEntity ORDER BY u.id")
                .pageSize(CHUNK_SIZE)
                .build();
    }

}
