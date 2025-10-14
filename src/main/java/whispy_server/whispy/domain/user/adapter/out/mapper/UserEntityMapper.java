package whispy_server.whispy.domain.user.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    UserJpaEntity toEntity(User userModel);
    User toModel(UserJpaEntity entity);

    default Optional<User> toOptionalDomain(Optional<UserJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }
}
