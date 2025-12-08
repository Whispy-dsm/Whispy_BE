package whispy_server.whispy.domain.admin.model;

import whispy_server.whispy.global.annotation.Aggregate;

import java.util.UUID;

/**
 * 관리자 도메인 모델
 * <p>
 * 헥사고날 아키텍처의 도메인 계층에 위치한 애그리게잇입니다.
 * 관리자의 핵심 비즈니스 정보를 표현하며, 인프라 기술에 독립적입니다.
 * </p>
 *
 * @param id 관리자 고유 식별자 (UUID)
 * @param adminId 관리자 로그인 ID
 * @param password 암호화된 비밀번호
 */
@Aggregate
public record Admin(
        UUID id,
        String adminId,
        String password
){}