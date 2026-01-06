package whispy_server.whispy.domain.music.adapter.out.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 음악 JPA 엔티티.
 *
 * tbl_music 테이블과 매핑되는 엔티티입니다.
 * 카테고리와 제목에 인덱스가 설정되어 있어 검색 성능이 최적화되어 있습니다.
 */
@Entity
@Table(
        name = "tbl_music",
        indexes = {
                @Index(name = "idx_category", columnList = "category"),
                @Index(name = "idx_title", columnList = "title")
        }
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MusicJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "artist", length = 200)
    private String artist;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MusicCategory category;

    @Column(name = "banner_image_url")
    private String bannerImageUrl;

    @Column(name = "video_url")
    private String videoUrl;
}
