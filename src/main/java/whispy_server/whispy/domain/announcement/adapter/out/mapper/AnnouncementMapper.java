package whispy_server.whispy.domain.announcement.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.announcement.adapter.out.entity.AnnouncementJpaEntity;
import whispy_server.whispy.domain.announcement.model.Announcement;

import java.util.List;
import java.util.Optional;

/**
 * 공지사항 매퍼 인터페이스.
 *
 * AnnouncementJpaEntity와 Announcement 도메인 모델 간의 변환을 담당하는 MapStruct 매퍼입니다.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnnouncementMapper {

    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     *
     * @param entity 공지사항 JPA 엔티티
     * @return 공지사항 도메인 모델
     */
    Announcement toModel(AnnouncementJpaEntity entity);

    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     *
     * @param model 공지사항 도메인 모델
     * @return 공지사항 JPA 엔티티
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    AnnouncementJpaEntity toEntity(Announcement model);

    /**
     * JPA 엔티티 리스트를 도메인 모델 리스트로 변환합니다.
     *
     * @param announcementJpaEntities 공지사항 JPA 엔티티 리스트
     * @return 공지사항 도메인 모델 리스트
     */
    List<Announcement> toModelList(List<AnnouncementJpaEntity> announcementJpaEntities);

    /**
     * Optional JPA 엔티티를 Optional 도메인 모델로 변환합니다.
     *
     * @param optionalEntity Optional 공지사항 JPA 엔티티
     * @return Optional 공지사항 도메인 모델
     */
    default Optional<Announcement> toOptionalModel(Optional<AnnouncementJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }

    /**
     * Page JPA 엔티티를 Page 도메인 모델로 변환합니다.
     *
     * @param entityPage 공지사항 JPA 엔티티 페이지
     * @return 공지사항 도메인 모델 페이지
     */
    default Page<Announcement> toModelPage(Page<AnnouncementJpaEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
