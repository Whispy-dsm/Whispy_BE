package whispy_server.whispy.domain.topic.application.port.out;

/**
 * 토픽 구독 삭제 포트.
 *
 * 토픽 구독 정보를 삭제하는 아웃바운드 포트입니다.
 */
public interface DeleteTopicSubscriptionPort {
    /**
     * 이메일로 토픽 구독을 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    void deleteByEmail(String email);
}
