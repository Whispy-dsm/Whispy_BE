package whispy_server.whispy.domain.reason.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.reason.adapter.out.entity.WithdrawalReasonJpaEntity;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;

import java.util.Optional;

/**
 * 탈퇴 사유 엔터티-도메인 변환 매퍼.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WithdrawalReasonMapper {

    WithdrawalReason toModel(WithdrawalReasonJpaEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    WithdrawalReasonJpaEntity toEntity(WithdrawalReason model);

    default Page<WithdrawalReason> toPageModel(Page<WithdrawalReasonJpaEntity> pageEntity) {
        return pageEntity.map(this::toModel);
    }

    default Optional<WithdrawalReason> toOptionalModel(Optional<WithdrawalReasonJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }
}
