package whispy_server.whispy.domain.admin.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.admin.adapter.out.entity.AdminJpaEntity;
import whispy_server.whispy.domain.admin.model.Admin;

import java.util.Optional;

/**
 * 관리자 엔티티 매퍼 인터페이스
 * <p>
 * MapStruct를 사용하여 Admin 도메인 모델과 AdminJpaEntity 간의 변환을 담당합니다.
 * 헥사고날 아키텍처에서 도메인과 영속성 계층 간의 경계를 관리합니다.
 * </p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminEntityMapper {

    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     *
     * @param entity 변환할 AdminJpaEntity
     * @return 변환된 Admin 도메인 모델
     */
    Admin toModel(AdminJpaEntity entity);

    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     *
     * @param adminDomain 변환할 Admin 도메인 모델
     * @return 변환된 AdminJpaEntity
     */
    AdminJpaEntity toEntity(Admin adminDomain);

    /**
     * Optional로 감싸진 JPA 엔티티를 Optional 도메인 모델로 변환합니다.
     *
     * @param optionalEntity 변환할 Optional<AdminJpaEntity>
     * @return 변환된 Optional<Admin>
     */
    default Optional<Admin> toOptionalModel(Optional<AdminJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }
}
