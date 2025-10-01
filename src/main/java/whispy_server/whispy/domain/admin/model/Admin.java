package whispy_server.whispy.domain.admin.model;

import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.annotation.Aggregate;

import java.util.UUID;

@Aggregate
public record Admin(
        UUID id,
        String adminId,
        String password
){}