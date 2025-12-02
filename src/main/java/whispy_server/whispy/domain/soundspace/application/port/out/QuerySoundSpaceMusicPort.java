package whispy_server.whispy.domain.soundspace.application.port.out;

import whispy_server.whispy.domain.soundspace.adapter.out.dto.SoundSpaceMusicWithDetailDto;

import java.util.List;

/**
 * 사운드 스페이스 데이터를 조회하기 위한 포트.
 */
public interface QuerySoundSpaceMusicPort {
    boolean existsByUserIdAndMusicId(Long userId, Long musicId);
    List<SoundSpaceMusicWithDetailDto> findSoundSpaceMusicsWithDetailByUserId(Long userId);
}
