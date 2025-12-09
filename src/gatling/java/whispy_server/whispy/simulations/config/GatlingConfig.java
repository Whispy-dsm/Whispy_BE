package whispy_server.whispy.simulations.config;

/**
 * Gatling 테스트 공통 설정
 * <p>
 * 모든 시뮬레이션에서 공통으로 사용하는 설정을 관리합니다.
 * JWT 토큰을 중앙에서 관리하여 토큰 변경 시 한 곳만 수정하면 됩니다.
 * </p>
 */
public class GatlingConfig {

    /**
     * 테스트용 JWT 토큰
     * <p>
     * 실제 테스트 전에 유효한 토큰으로 교체 필요
     * 토큰이 만료되면 이 값만 변경하면 모든 시뮬레이션에 적용됩니다.
     * </p>
     */
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MkBnYW1pbC5jb20iLCJ0eXBlIjoiYWNjZXNzX3Rva2VuIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NjUyODM3ODMsImV4cCI6MTc2NTI4NzM4M30.RWxess9oXfezHMENxp0zWBUfYyvEfNod2SxXvPnzYlk";

    /**
     * 베이스 URL
     * 로컬 테스트 시 localhost, 운영 환경 테스트 시 실제 서버 URL로 변경
     */
    public static final String BASE_URL = "http://localhost:8080";

    /**
     * Authorization 헤더 값
     * Bearer 토큰 형식으로 반환
     */
    public static String getAuthorizationHeader() {
        return "Bearer " + JWT_TOKEN;
    }
}
