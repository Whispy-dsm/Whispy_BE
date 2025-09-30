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

@Service
@RequiredArgsConstructor
public class AdminLoginService implements AdminLoginUseCase {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final QueryAdminPort queryAdminPort;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public TokenResponse execute(AdminLoginRequest request) {
        Admin admin = queryAdminPort.findByAdminId(request.adminId())
                .orElseThrow(() -> AdminNotFoundException.EXCEPTION);

        if(!passwordEncoder.matches(request.password(), admin.password())) {
            throw PasswordMissMatchException.EXCEPTION;
        }

        TokenResponse tokenResponse = jwtTokenProvider.generateToken(admin.id().toString(), Role.ADMIN.name());
        RefreshToken token = new RefreshToken(
                admin.id().toString(),
                tokenResponse.refreshToken(),
                jwtProperties.refreshExpiration()
        );
        refreshTokenRepository.save(token);
        return tokenResponse;
    }
}
