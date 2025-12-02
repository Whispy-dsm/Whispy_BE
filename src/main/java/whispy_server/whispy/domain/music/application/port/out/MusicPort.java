package whispy_server.whispy.domain.music.application.port.out;

/**
 * 음악 아웃바운드 포트.
 *
 * 음악 도메인의 모든 아웃바운드 포트를 통합한 인터페이스입니다.
 * MusicSavePort, MusicDeletePort, QueryMusicPort, SearchMusicPort를 상속합니다.
 */
public interface MusicPort extends MusicSavePort, MusicDeletePort, QueryMusicPort, SearchMusicPort {
}
