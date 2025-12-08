package whispy_server.whispy.domain.topic.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

/**
 * 토픽 구독 JPA 엔티티.
 *
 * 사용자의 FCM 토픽 구독 정보를 저장하는 엔티티입니다.
 */
@Entity(name = "TopicSubscriptionJpaEntity")
@Table(name = "tbl_topic_subscription", indexes = {
        @Index(name = "idx_topic_sub_email", columnList = "email"),
        @Index(name = "idx_topic_sub_topic_subscribed", columnList = "topic, subscribed")
    },
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_topic_sub_email_topic", columnNames = {"email", "topic"})
    }
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicSubscriptionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic", nullable = false, length = 50)
    private NotificationTopic topic;

    @Column(name = "subscribed", nullable = false)
    private boolean subscribed; // IsSubscribed -> Subscribed로 변경 ( mapstruct 이슈 )
}
