package whispy_server.whispy.domain.sleepsession.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.sleepsession.adapter.out.mapper.SleepSessionMapper;
import whispy_server.whispy.domain.sleepsession.adapter.out.persistence.repository.SleepSessionRepository;
import whispy_server.whispy.domain.sleepsession.application.port.out.SleepSessionPort;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.util.Optional;

/**
 * 수면 세션 영속성 어댑터.
 *
 * JPA를 통한 수면 세션의 저장, 조회, 삭제 기능을 구현하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class SleepSessionPersistenceAdapter implements SleepSessionPort {

    private final SleepSessionRepository sleepSessionRepository;
    private final SleepSessionMapper sleepSessionMapper;

    /**
     * 수면 세션을 저장합니다.
     *
     * @param sleepSession 저장할 도메인 모델
     * @return 저장된 세션의 도메인 모델 (ID 포함)
     */
    @Override
    public SleepSession save(SleepSession sleepSession) {
        return sleepSessionMapper.toModel(
                sleepSessionRepository.save(
                        sleepSessionMapper.toEntity(sleepSession)
                )
        );
    }

    /**
     * 사용자 ID로 수면 세션을 페이지 단위로 조회합니다.
     * 시작 일시의 역순으로 정렬됩니다.
     *
     * @param userId 조회할 사용자 ID
     * @param pageable 페이지 정보
     * @return 수면 세션 페이지
     */
    @Override
    public Page<SleepSession> findByUserId(Long userId, Pageable pageable) {
        return sleepSessionMapper.toModelPage(sleepSessionRepository.findByUserIdOrderByStartedAtDesc(userId, pageable));
    }

    /**
     * 세션 ID와 사용자 ID로 특정 수면 세션을 조회합니다.
     *
     * @param id 조회할 세션 ID
     * @param userId 세션의 사용자 ID
     * @return 조회된 세션의 선택적 도메인 모델
     */
    @Override
    public Optional<SleepSession> findByIdAndUserId(Long id, Long userId) {
        return sleepSessionMapper.toOptionalModel(sleepSessionRepository.findByIdAndUserId(id, userId));
    }

    /**
     * 세션 ID로 수면 세션을 삭제합니다.
     *
     * @param id 삭제할 세션 ID
     */
    @Override
    public void deleteById(Long id) {
        sleepSessionRepository.deleteById(id);
    }

    /**
     * 사용자 ID로 해당 사용자의 모든 수면 세션을 삭제합니다.
     *
     * @param userId 삭제할 사용자의 ID
     */
    @Override
    public void deleteByUserId(Long userId) {
        sleepSessionRepository.deleteByUserId(userId);
    }
}
