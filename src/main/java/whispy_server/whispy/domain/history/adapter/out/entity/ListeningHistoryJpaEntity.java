package whispy_server.whispy.domain.history.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.LocalDateTime;

/**
 * 청취 이력을 저장하는 JPA 엔터티.
 */
@Entity
@Table(name = "tbl_listening_history",
        indexes = {
                @Index(name = "idx_listening_history_user_listened", columnList = "user_id, listened_at"),
                @Index(name = "idx_listening_history_music_id", columnList = "music_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "music_id"})
        })
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListeningHistoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "music_id", nullable = false)
    private Long musicId;

    @Column(name = "listened_at", nullable = false)
    private LocalDateTime listenedAt;
}
