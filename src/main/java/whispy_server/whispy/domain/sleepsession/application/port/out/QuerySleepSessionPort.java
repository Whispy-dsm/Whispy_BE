package whispy_server.whispy.domain.sleepsession.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.util.Optional;

/**
 * 수면 세션 조회 포트 인터페이스.
 *
 * 수면 세션 조회 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface QuerySleepSessionPort {

    /**
     * 사용자 ID로 수면 세션을 페이지 단위로 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @param pageable 페이지 정보
     * @return 수면 세션 페이지
     */
    Page<SleepSession> findByUserId(Long userId, Pageable pageable);

    /**
     * 세션 ID와 사용자 ID로 특정 수면 세션을 조회합니다.
     *
     * @param id 조회할 세션 ID
     * @param userId 세션의 사용자 ID
     * @return 조회된 세션의 선택적 도메인 모델
     */
    Optional<SleepSession> findByIdAndUserId(Long id, Long userId);
}
