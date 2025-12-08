package whispy_server.whispy.domain.admin.application.port.out;

/**
 * 관리자 아웃바운드 포트 인터페이스
 * <p>
 * 헥사고날 아키텍처의 아웃바운드 포트로, QueryAdminPort를 확장합니다.
 * 관리자 도메인의 모든 데이터 접근 작업을 통합하는 마커 인터페이스입니다.
 * 현재는 조회 기능만 제공하지만, 향후 CUD 작업이 필요한 경우 확장될 수 있습니다.
 * </p>
 */
public interface AdminPort extends QueryAdminPort{
}
