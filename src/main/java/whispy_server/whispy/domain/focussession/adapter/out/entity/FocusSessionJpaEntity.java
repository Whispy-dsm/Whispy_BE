package whispy_server.whispy.domain.focussession.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

/**
 * 집중 세션 JPA 엔티티.
 *
 * 집중 세션 정보를 데이터베이스에 영속화하기 위한 JPA 엔티티입니다.
 * 사용자 ID와 시작 일시에 대한 복합 인덱스를 포함합니다.
 */
@Entity(name = "FocusSessionJpaEntity")
@Table(name = "tbl_focus_session", indexes = {
    @Index(name = "idx_user_started", columnList = "user_id, started_at")
})
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FocusSessionJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at", nullable = false)
    private LocalDateTime endedAt;

    @Column(name = "duration_seconds", nullable = false)
    private int durationSeconds;

    @Column(name = "tag", nullable = false)
    @Enumerated(EnumType.STRING)
    private FocusTag tag;
}