package whispy_server.whispy.domain.notification.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;
import whispy_server.whispy.domain.notification.batch.dto.DeleteOldNotificationJobParameters;

/**
 * 오래된 알림 삭제 Item Processor.
 *
 * NotificationJpaEntity를 DeleteOldNotificationJobParameters로 변환하는 배치 프로세서입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteOldNotificationItemProcessor implements ItemProcessor<NotificationJpaEntity, DeleteOldNotificationJobParameters> {

    /**
     * 알림 엔티티를 삭제 파라미터로 변환합니다.
     *
     * @param entity 알림 JPA 엔티티
     * @return 삭제 파라미터
     * @throws Exception 변환 중 예외 발생 시
     */
    @Override
    public DeleteOldNotificationJobParameters process(NotificationJpaEntity entity) throws Exception {
        return new DeleteOldNotificationJobParameters(entity.getId());
    }
}
