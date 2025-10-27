package whispy_server.whispy.domain.payment.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.payment.adapter.out.entity.SubscriptionJpaEntity;
import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionEntityMapper {

    Subscription toModel(SubscriptionJpaEntity entity);
    
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    SubscriptionJpaEntity toEntity(Subscription subscription);

    default Optional<Subscription> toOptionalModel(Optional<SubscriptionJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }
}
