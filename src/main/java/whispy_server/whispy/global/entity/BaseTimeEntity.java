package whispy_server.whispy.global.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import whispy_server.whispy.global.config.jpa.JpaAuditingConfig;

import java.time.LocalDateTime;

/**
 * 생성/수정 시간을 공통으로 관리하는 추상 엔터티.
 *
 * {@link JpaAuditingConfig}에서 Auditing 기능을 활성화해야 동작한다.
 */
@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
