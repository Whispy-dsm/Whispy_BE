package whispy_server.whispy.domain.topic.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.batch.dto.AddTopicJobParameters;
import whispy_server.whispy.domain.topic.model.TopicSubscription;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddTopicItemWriter implements ItemWriter<AddTopicJobParameters> {

    private final SaveTopicSubscriptionPort saveTopicSubscriptionPort;
    private final FcmSendPort fcmSendPort;

    @Override
    public void write(Chunk<? extends AddTopicJobParameters> chunk) throws Exception {
        try {

            List<TopicSubscription> subscriptions = chunk.getItems().stream()
                    .map(item -> new TopicSubscription(
                            null,
                            item.email(),
                            item.topic(),
                            item.defaultSubscribed()
                    ))
                    .toList();

            saveTopicSubscriptionPort.saveAll(subscriptions);

            chunk.getItems().stream()
                    .filter(item -> item.defaultSubscribed() && item.fcmToken() != null)
                    .forEach(item -> {
                        try {
                            fcmSendPort.subscribeToTopic(item.fcmToken(), item.topic());
                        } catch (Exception e) {
                            log.warn("사용자 {}의 FCM 구독 실패: {}", item.email(), e.getMessage());
                        }
                    });
            log.info("새 토픽 배치 청크 처리 완료: {}건", chunk.size());

        } catch (Exception e) {
            log.error("새 토픽 배치 청크 처리 실패: {}", e.getMessage(), e);
            throw e; // 실패 시 배치 중단
        }
    }
}
