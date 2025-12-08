package whispy_server.whispy.domain.user.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;

import java.util.Optional;

/**
 * User 도메인 모델과 UserJpaEntity 간의 매핑을 담당하는 MapStruct 매퍼.
 * 도메인 계층과 영속성 계층 간의 변환을 처리합니다.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    /**
     * User 도메인 모델을 UserJpaEntity로 변환합니다.
     *
     * @param userModel User 도메인 모델
     * @return UserJpaEntity 엔티티
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    UserJpaEntity toEntity(User userModel);

    /**
     * UserJpaEntity를 User 도메인 모델로 변환합니다.
     *
     * @param entity UserJpaEntity 엔티티
     * @return User 도메인 모델
     */
    User toModel(UserJpaEntity entity);

    /**
     * Optional UserJpaEntity를 Optional User 도메인 모델로 변환합니다.
     *
     * @param optionalEntity Optional UserJpaEntity
     * @return Optional User 도메인 모델
     */
    default Optional<User> toOptionalDomain(Optional<UserJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }
}
