package whispy_server.whispy.domain.meditationsession.adapter.out.entity;

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
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;
import whispy_server.whispy.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity(name = "MeditationSessionJpaEntity")
@Table(name = "tbl_meditation_session", indexes = {
    @Index(name = "idx_user_started", columnList = "user_id, started_at")
})
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeditationSessionJpaEntity extends BaseTimeEntity {

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

    @Column(name = "breathe_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    private BreatheMode breatheMode;
}
