package whispy_server.whispy.global.oauth.dto;

/**
 * OAuth 프로바이더에서 획득한 사용자 기본 정보를 담는 DTO.
 */
public record OauthUserInfo(String name, String email, String profileImage) { }
