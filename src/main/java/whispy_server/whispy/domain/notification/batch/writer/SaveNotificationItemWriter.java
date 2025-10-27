package whispy_server.whispy.domain.notification.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.notification.batch.dto.NotificationJobParameters;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveNotificationItemWriter implements ItemWriter<NotificationJobParameters> {

    private final SaveNotificationPort saveNotificationPort;

    @Override
    public void write(Chunk<? extends NotificationJobParameters> chunk) {
        try {
            List<Notification> notifications = chunk.getItems().stream()
                    .map(item -> new Notification(
                            null,
                            item.email(),
                            item.title(),
                            item.body(),
                            item.topic(),
                            item.data(),
                            false,
                            null
                    )).toList();

            saveNotificationPort.saveAll(notifications);

        } catch (Exception e) {
            throw BatchJobExecutionFailedException.EXCEPTION;
        }
    }

}
