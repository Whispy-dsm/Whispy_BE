package whispy_server.whispy.domain.focussession.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.focussession.model.FocusSession;

import java.util.Optional;

/**
 * 집중 세션 조회 포트 인터페이스.
 *
 * 집중 세션 조회 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface QueryFocusSessionPort {

    /**
     * 사용자 ID로 집중 세션을 페이지 단위로 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @param pageable 페이지 정보
     * @return 집중 세션 페이지
     */
    Page<FocusSession> findByUserId(Long userId, Pageable pageable);

    /**
     * 세션 ID와 사용자 ID로 특정 집중 세션을 조회합니다.
     *
     * @param id 조회할 세션 ID
     * @param userId 세션의 사용자 ID
     * @return 조회된 세션의 선택적 도메인 모델
     */
    Optional<FocusSession> findByIdAndUserId(Long id, Long userId);
}
