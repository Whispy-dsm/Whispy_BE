package whispy_server.whispy.global.support;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.global.config.TestFcmConfig;

/**
 * 통합 테스트 베이스 클래스.
 *
 * 모든 통합 테스트는 이 클래스를 상속받아 공통 설정을 재사용합니다.
 * H2 인메모리 DB와 MockMvc를 사용하여 전체 애플리케이션 컨텍스트에서 테스트합니다.
 *
 * 포함된 설정:
 * - SpringBootTest: 전체 애플리케이션 컨텍스트 로드
 * - AutoConfigureMockMvc: MockMvc 자동 설정
 * - ActiveProfiles("test"): 테스트 프로파일 활성화
 * - Transactional: 각 테스트 후 자동 롤백
 * - TestFcmConfig: FCM Mock 설정
 * - Spring Batch 비활성화
 */
@SpringBootTest(properties = {
        "spring.batch.job.enabled=false"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestFcmConfig.class)
public abstract class IntegrationTestSupport {
}
