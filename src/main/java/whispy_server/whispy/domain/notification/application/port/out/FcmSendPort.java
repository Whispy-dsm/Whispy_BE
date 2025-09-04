package whispy_server.whispy.domain.notification.application.port.out;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Map;

public interface FcmSendPort {
    void sendMulticast(List<String> tokens, String title, String body, Map<String, String> data);
    void sendToTopic(NotificationTopic topic, String title, String body, Map<String, String> data);
    void subscribeToTopic(String deviceToken, NotificationTopic topic);
    void unsubscribeFromTopic(String deviceToken, NotificationTopic topic);
}
