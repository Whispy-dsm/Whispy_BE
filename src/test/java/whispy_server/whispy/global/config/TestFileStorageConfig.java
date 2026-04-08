package whispy_server.whispy.global.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import whispy_server.whispy.domain.file.adapter.out.external.InMemoryFileStorageAdapter;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;

/**
 * 테스트 환경에서 사용할 파일 저장소 Mock 설정.
 *
 * 통합 테스트에서 실제 Cloudflare R2 연결 없이 테스트할 수 있도록
 * 메모리 기반 FileStoragePort 구현체를 제공합니다.
 */
@TestConfiguration
public class TestFileStorageConfig {

    /**
     * 테스트 전용 메모리 기반 파일 저장소 빈을 생성합니다.
     *
     * @return 테스트용 FileStoragePort 구현체
     */
    @Bean
    @Primary
    public FileStoragePort fileStoragePort() {
        return new InMemoryFileStorageAdapter();
    }
}
