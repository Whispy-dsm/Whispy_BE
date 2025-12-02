package whispy_server.whispy.domain.music.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.music.adapter.out.entity.MusicJpaEntity;
import whispy_server.whispy.domain.music.adapter.out.mapper.MusicMapper;
import whispy_server.whispy.domain.music.adapter.out.persistence.repository.MusicJpaRepository;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

import java.util.Optional;

/**
 * 음악 영속성 어댑터.
 *
 * 음악 도메인의 데이터베이스 접근을 담당하는 아웃바운드 어댑터입니다.
 * MusicPort를 구현하여 도메인 계층에 영속성 기능을 제공합니다.
 */
@Component
@RequiredArgsConstructor
public class MusicPersistenceAdapter implements MusicPort {

    private final MusicJpaRepository musicJpaRepository;
    private final MusicMapper musicMapper;

    /**
     * 음악을 저장합니다.
     *
     * @param music 저장할 음악 도메인 모델
     * @return 저장된 음악 도메인 모델
     */
    @Override
    public Music save(Music music) {
        MusicJpaEntity entity = musicMapper.toEntity(music);
        MusicJpaEntity savedEntity = musicJpaRepository.save(entity);
        return musicMapper.toModel(savedEntity);
    }

    /**
     * ID로 음악을 조회합니다.
     *
     * @param id 조회할 음악 ID
     * @return Optional 음악 도메인 모델
     */
    @Override
    public Optional<Music> findById(Long id) {
        return musicMapper.toOptionalModel(musicJpaRepository.findById(id));
    }

    /**
     * 음악 ID의 존재 여부를 확인합니다.
     *
     * @param id 확인할 음악 ID
     * @return 존재하면 true, 아니면 false
     */
    @Override
    public boolean existsById(Long id) {
        return musicJpaRepository.existsById(id);
    }

    /**
     * ID로 음악을 삭제합니다.
     *
     * @param id 삭제할 음악 ID
     */
    @Override
    public void deleteById(Long id) {
        musicJpaRepository.deleteById(id);
    }

    /**
     * 제목으로 음악을 검색합니다.
     *
     * @param title 검색할 제목 키워드
     * @param pageable 페이지 정보
     * @return 음악 도메인 모델 페이지
     */
    @Override
    public Page<Music> searchByTitle(String title, Pageable pageable) {
        return musicMapper.toPageModel(musicJpaRepository.findByTitleContaining(title, pageable));
    }

    /**
     * 카테고리로 음악을 검색합니다.
     *
     * @param category 검색할 카테고리
     * @param pageable 페이지 정보
     * @return 음악 도메인 모델 페이지
     */
    @Override
    public Page<Music> searchByCategory(MusicCategory category, Pageable pageable) {
        return musicMapper.toPageModel(musicJpaRepository.findByCategory(category, pageable));
    }
}
