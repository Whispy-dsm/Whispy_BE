package whispy_server.whispy.domain.soundspace.application.port.out;

import whispy_server.whispy.domain.soundspace.adapter.out.dto.SoundSpaceMusicWithDetailDto;

import java.util.List;

public interface QuerySoundSpaceMusicPort {
    boolean existsByUserIdAndMusicId(Long userId, Long musicId);
    List<SoundSpaceMusicWithDetailDto> findSoundSpaceMusicsWithDetailByUserId(Long userId);
}
