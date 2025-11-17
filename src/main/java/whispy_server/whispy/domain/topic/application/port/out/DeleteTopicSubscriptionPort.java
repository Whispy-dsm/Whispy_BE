package whispy_server.whispy.domain.topic.application.port.out;

public interface DeleteTopicSubscriptionPort {
    void deleteByEmail(String email);
}
