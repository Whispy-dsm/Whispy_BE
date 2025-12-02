package whispy_server.whispy.domain.history.application.port.out;

/**
 * 청취 이력 삭제 Port.
 */
public interface DeleteListeningHistoryPort {
    /**
     * 음악 기준으로 청취 이력을 삭제한다.
     */
    void deleteAllByMusicId(Long musicId);

    /**
     * 사용자 기준으로 청취 이력을 삭제한다.
     */
    void deleteByUserId(Long userId);
}
