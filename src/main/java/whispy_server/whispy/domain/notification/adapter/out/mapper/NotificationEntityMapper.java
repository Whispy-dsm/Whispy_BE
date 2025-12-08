package whispy_server.whispy.domain.notification.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;
import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

/**
 * 알림 엔티티 매퍼.
 *
 * NotificationJpaEntity와 Notification 도메인 모델 간의 변환을 담당하는 MapStruct 매퍼입니다.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationEntityMapper {

    /**
     * Notification 도메인 모델을 JPA 엔티티로 변환합니다.
     *
     * @param notification 알림 도메인 모델
     * @return 알림 JPA 엔티티
     */
    NotificationJpaEntity toEntity(Notification notification);

    /**
     * NotificationJpaEntity를 도메인 모델로 변환합니다.
     *
     * @param notificationJpaEntity 알림 JPA 엔티티
     * @return 알림 도메인 모델
     */
    Notification toModel(NotificationJpaEntity notificationJpaEntity);

    /**
     * NotificationJpaEntity 리스트를 도메인 모델 리스트로 변환합니다.
     *
     * @param notificationJpaEntityList 알림 JPA 엔티티 리스트
     * @return 알림 도메인 모델 리스트
     */
    List<Notification> toModelList(List<NotificationJpaEntity> notificationJpaEntityList);

    /**
     * Notification 리스트를 JPA 엔티티 리스트로 변환합니다.
     *
     * @param notifications 알림 도메인 모델 리스트
     * @return 알림 JPA 엔티티 리스트
     */
    List<NotificationJpaEntity> toEntityList(List<Notification> notifications);

    /**
     * Optional<NotificationJpaEntity>를 Optional<Notification>으로 변환합니다.
     *
     * @param optionalEntity 알림 JPA 엔티티 Optional
     * @return 알림 도메인 모델 Optional
     */
    default Optional<Notification> toOptionalModel(Optional<NotificationJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }

    /**
     * Page<NotificationJpaEntity>를 Page<Notification>으로 변환합니다.
     *
     * @param entityPage 알림 JPA 엔티티 페이지
     * @return 알림 도메인 모델 페이지
     */
    default Page<Notification> toModelPage(Page<NotificationJpaEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
