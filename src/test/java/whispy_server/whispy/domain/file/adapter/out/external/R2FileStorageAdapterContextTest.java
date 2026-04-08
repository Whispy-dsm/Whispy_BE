package whispy_server.whispy.domain.file.adapter.out.external;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.services.s3.S3Client;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.global.config.r2.R2Config;
import whispy_server.whispy.global.file.R2Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * R2 파일 저장소 빈 등록 조건을 검증한다.
 */
@DisplayName("R2FileStorageAdapter 컨텍스트 테스트")
class R2FileStorageAdapterContextTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(R2FileStorageTestConfig.class)
            .withPropertyValues(
                    "spring.whispy.r2.account-id=test-account",
                    "spring.whispy.r2.access-key-id=test-access-key",
                    "spring.whispy.r2.secret-access-key=test-secret-key",
                    "spring.whispy.r2.bucket=test-bucket"
            );

    /**
     * test 프로파일이 아니면 R2 기반 FileStoragePort 구현체가 등록되어야 한다.
     */
    @Test
    @DisplayName("local 프로파일에서는 FileStoragePort 빈이 등록된다")
    void registersFileStoragePortOutsideTestProfile() {
        contextRunner
                .withInitializer(context -> context.getEnvironment().setActiveProfiles("local"))
                .run(context -> {
                    assertThat(context).hasSingleBean(S3Client.class);
                    assertThat(context).hasSingleBean(FileStoragePort.class);
                    assertThat(context.getBean(FileStoragePort.class)).isInstanceOf(R2FileStorageAdapter.class);
                });
    }

    /**
     * test 프로파일에서는 실제 R2 어댑터와 S3Client 가 등록되면 안 된다.
     */
    @Test
    @DisplayName("test 프로파일에서는 R2 관련 빈을 등록하지 않는다")
    void doesNotRegisterR2BeansInTestProfile() {
        contextRunner
                .withInitializer(context -> context.getEnvironment().setActiveProfiles("test"))
                .run(context -> {
                    assertThat(context).doesNotHaveBean(S3Client.class);
                    assertThat(context).doesNotHaveBean(FileStoragePort.class);
                });
    }

    /**
     * R2 구성과 어댑터 스캔을 함께 로드하는 테스트 전용 설정이다.
     */
    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(R2Properties.class)
    @Import(R2Config.class)
    @ComponentScan(basePackageClasses = R2FileStorageAdapter.class)
    static class R2FileStorageTestConfig {
    }
}
