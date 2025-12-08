package whispy_server.whispy.domain.notification.adapter.out.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.entity.BaseTimeEntity;

import java.util.Map;

/**
 * 알림 JPA 엔티티.
 *
 * 알림 정보를 데이터베이스에 저장하기 위한 JPA 엔티티입니다.
 * 이메일과 생성일시, 이메일과 읽음 여부에 대한 복합 인덱스가 설정되어 있습니다.
 */
@Entity(name = "NotificationJpaEntity")
@Table(name = "tbl_notification",
        indexes = {
                @Index(name = "idx_notification_email_created", columnList = "email, created_at"),
                @Index(name = "idx_notification_email_read", columnList = "email, `read`")
        })
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationJpaEntity extends BaseTimeEntity {

    /**
     * 알림 ID (기본 키).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 수신자 이메일.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 알림 제목.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 알림 본문.
     */
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    /**
     * 알림 토픽.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "topic", nullable = false, length = 50)
    private NotificationTopic topic;

    /**
     * 추가 데이터 (키-값 맵).
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "notification_data", joinColumns = @JoinColumn(name = "notification_id"))
    @MapKeyColumn(name = "data_key")
    @Column(name = "data_value")
    private Map<String, String> data;

    /**
     * 읽음 여부.
     */
    @Column(name = "`read`", nullable = false)
    private boolean read; // isRead -> read로 바꿈 ( mapStruct 이슈 )

}
