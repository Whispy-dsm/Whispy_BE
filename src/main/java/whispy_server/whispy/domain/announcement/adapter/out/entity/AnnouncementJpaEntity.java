package whispy_server.whispy.domain.announcement.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import whispy_server.whispy.global.entity.BaseTimeEntity;

/**
 * 공지사항 JPA 엔티티.
 *
 * tbl_announcement 테이블과 매핑되는 엔티티입니다.
 * BaseTimeEntity를 상속하여 생성일시, 수정일시를 자동으로 관리합니다.
 */
@Entity(name = "AnnouncementJpaEntity")
@Table(name = "tbl_announcement")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AnnouncementJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "banner_image_url")
    private String bannerImageUrl;
}
