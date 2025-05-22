package whispy_server.whispy.domain.admin.domain;

import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.global.annotation.Aggregate;

import java.util.UUID;

@Aggregate
public record Admin(
        UUID id,
        String adminId,
        String password,
        Role role,
        String name
){}