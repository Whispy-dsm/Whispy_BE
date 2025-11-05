package whispy_server.whispy.domain.music.adapter.out.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.entity.BaseTimeEntity;

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

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false)
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MusicCategory category;

    @Column(length = 500)
    private String bannerImageUrl;
}
