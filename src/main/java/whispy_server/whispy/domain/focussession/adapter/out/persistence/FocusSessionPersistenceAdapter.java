package whispy_server.whispy.domain.focussession.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.focussession.adapter.out.mapper.FocusSessionMapper;
import whispy_server.whispy.domain.focussession.adapter.out.persistence.repository.FocusSessionRepository;
import whispy_server.whispy.domain.focussession.application.port.out.FocusSessionPort;
import whispy_server.whispy.domain.focussession.model.FocusSession;

import java.util.Optional;

/**
 * 집중 세션 영속성 어댑터.
 *
 * JPA를 통한 집중 세션의 저장, 조회, 삭제 기능을 구현하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class FocusSessionPersistenceAdapter implements FocusSessionPort {

    private final FocusSessionRepository focusSessionRepository;
    private final FocusSessionMapper focusSessionMapper;

    /**
     * 집중 세션을 저장합니다.
     *
     * @param focusSession 저장할 도메인 모델
     * @return 저장된 세션의 도메인 모델 (ID 포함)
     */
    @Override
    public FocusSession save(FocusSession focusSession) {
        return focusSessionMapper.toModel(
                focusSessionRepository.save(
                        focusSessionMapper.toEntity(focusSession)
                )
        );
    }

    /**
     * 사용자 ID로 집중 세션을 페이지 단위로 조회합니다.
     * 시작 일시의 역순으로 정렬됩니다.
     *
     * @param userId 조회할 사용자 ID
     * @param pageable 페이지 정보
     * @return 집중 세션 페이지
     */
    @Override
    public Page<FocusSession> findByUserId(Long userId, Pageable pageable) {
        return focusSessionMapper.toModelPage(focusSessionRepository.findByUserIdOrderByStartedAtDesc(userId, pageable));
    }

    /**
     * 세션 ID와 사용자 ID로 특정 집중 세션을 조회합니다.
     *
     * @param id 조회할 세션 ID
     * @param userId 세션의 사용자 ID
     * @return 조회된 세션의 선택적 도메인 모델
     */
    @Override
    public Optional<FocusSession> findByIdAndUserId(Long id, Long userId) {
        return focusSessionMapper.toOptionalModel(focusSessionRepository.findByIdAndUserId(id, userId));
    }

    /**
     * 세션 ID로 집중 세션을 삭제합니다.
     *
     * @param id 삭제할 세션 ID
     */
    @Override
    public void deleteById(Long id) {
        focusSessionRepository.deleteById(id);
    }

    /**
     * 사용자 ID로 해당 사용자의 모든 집중 세션을 삭제합니다.
     *
     * @param userId 삭제할 사용자의 ID
     */
    @Override
    public void deleteByUserId(Long userId) {
        focusSessionRepository.deleteByUserId(userId);
    }
}
