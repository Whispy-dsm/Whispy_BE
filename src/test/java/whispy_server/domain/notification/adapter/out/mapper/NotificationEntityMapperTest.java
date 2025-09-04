package whispy_server.whispy.domain.notification.adapter.out.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationEntityMapperTest {

    private final NotificationEntityMapper notificationEntityMapper =
            Mappers.getMapper(NotificationEntityMapper.class);

    @Test
    void testMapping() {
        // given
        NotificationJpaEntity entity = NotificationJpaEntity.builder()
                .email("test@test.com")
                .title("title")
                .body("body")
                .topic(NotificationTopic.SYSTEM_ANNOUNCEMENT)
                .isRead(true)
                .build();

        // when
        Notification model = notificationEntityMapper.toModel(entity);

        // then
        System.out.println("isRead = " + model.isRead());
        assertThat(model.isRead()).isTrue();  // true여야 정상
    }
}
