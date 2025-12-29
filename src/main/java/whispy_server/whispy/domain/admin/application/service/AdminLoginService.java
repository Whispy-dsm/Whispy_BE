package whispy_server.whispy.domain.admin.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.admin.adapter.in.web.dto.request.AdminLoginRequest;
import whispy_server.whispy.domain.admin.application.port.in.AdminLoginUseCase;
import whispy_server.whispy.domain.admin.application.port.out.QueryAdminPort;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.exception.domain.admin.AdminNotFoundException;
import whispy_server.whispy.global.exception.domain.user.PasswordMissMatchException;
import whispy_server.whispy.global.security.jwt.JwtProperties;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;
import whispy_server.whispy.global.security.jwt.domain.entity.RefreshToken;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

/**
 * 관리자 로그인 서비스.
 *
 * 관리자 인증을 처리하고 JWT 토큰을 발급하는 유스케이스 구현체입니다.
 * 비밀번호 검증 후 액세스 토큰과 리프레시 토큰을 생성하여 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class AdminLoginService implements AdminLoginUseCase {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final QueryAdminPort queryAdminPort;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 관리자 로그인을 실행합니다.
     *
     * 관리자 ID로 계정을 조회하고 비밀번호를 검증합니다.
     * 인증 성공 시 JWT 토큰을 생성하고 리프레시 토큰을 Redis에 저장합니다.
     *
     * @param request 관리자 ID와 비밀번호가 포함된 요청
     * @return JWT 액세스 토큰과 리프레시 토큰
     * @throws AdminNotFoundException 관리자를 찾을 수 없는 경우
     * @throws PasswordMissMatchException 비밀번호가 일치하지 않는 경우
     */
    @Override
    @Transactional
    public TokenResponse execute(AdminLoginRequest request) {
        Admin admin = queryAdminPort.findByAdminId(request.adminId())
                .orElseThrow(() -> AdminNotFoundException.EXCEPTION);

        if(!passwordEncoder.matches(request.password(), admin.password())) {
            throw PasswordMissMatchException.EXCEPTION;
        }

        TokenResponse tokenResponse = jwtTokenProvider.generateToken(admin.id(), Role.ADMIN.name());
        RefreshToken token = new RefreshToken(
                admin.id(),
                tokenResponse.refreshToken(),
                jwtProperties.refreshExpiration()
        );
        refreshTokenRepository.save(token);
        return tokenResponse;
    }
}
