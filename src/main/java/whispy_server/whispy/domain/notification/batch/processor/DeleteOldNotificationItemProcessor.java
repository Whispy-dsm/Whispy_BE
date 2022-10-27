package whispy_server.whispy.domain.notification.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;
import whispy_server.whispy.domain.notification.batch.dto.DeleteOldNotificationJobParameters;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteOldNotificationItemProcessor implements ItemProcessor<NotificationJpaEntity, DeleteOldNotificationJobParameters> {

    @Override
    public DeleteOldNotificationJobParameters process(NotificationJpaEntity entity) throws Exception {
        return new DeleteOldNotificationJobParameters(entity.getId());
    }
}
