package whispy_server.whispy.domain.soundspace.adapter.out.entity;

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
 * 사용자 사운드 스페이스에 저장된 음악을 나타내는 엔티티.
 */
@Entity(name = "SoundSpaceMusicJpaEntity")
@Table(name = "tbl_soundspace_music",
        indexes = {
                @Index(name = "idx_soundspace_user_added", columnList = "user_id, added_at"),
                @Index(name = "idx_soundspace_music_id", columnList = "music_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "music_id"})
        })
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SoundSpaceMusicJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "music_id", nullable = false)
    private Long musicId;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;
}
