package whispy_server.whispy.domain.payment.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.payment.adapter.out.entity.SubscriptionJpaEntity;
import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

/**
 * 구독 엔티티 매퍼.
 *
 * 구독 도메인 모델과 JPA 엔티티 간의 변환을 담당하는 MapStruct 매퍼입니다.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionEntityMapper {

    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     *
     * @param entity JPA 엔티티
     * @return 도메인 모델
     */
    Subscription toModel(SubscriptionJpaEntity entity);

    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     *
     * @param subscription 도메인 모델
     * @return JPA 엔티티
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    SubscriptionJpaEntity toEntity(Subscription subscription);

    /**
     * Optional JPA 엔티티를 Optional 도메인 모델로 변환합니다.
     *
     * @param optionalEntity Optional JPA 엔티티
     * @return Optional 도메인 모델
     */
    default Optional<Subscription> toOptionalModel(Optional<SubscriptionJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }
}
