package whispy_server.whispy.global.config.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Spring Batch 기능을 활성화하는 설정 클래스.
 *
 *
 * tbl_batch_ 프리픽스를 사용하는 메타데이터 테이블을 위한 설정을 제공합니다.
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfiguration {

}
