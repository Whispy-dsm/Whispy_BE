package whispy_server.whispy.simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import whispy_server.whispy.simulations.config.GatlingConfig;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Whispy 포트폴리오용 통합 성능 테스트 시뮬레이션
 * <p>
 * 핵심 3개 API(수면 통계, 음악 검색, 수면 세션)를 통합하여
 * 실제 사용자 행동 패턴을 반영한 종합 성능 테스트를 수행합니다.
 * </p>
 * <p>
 * 포트폴리오 작성을 위한 주요 성능 지표:
 * - TPS (Transactions Per Second): 초당 처리 트랜잭션 수
 * - 95 Percentile 응답시간: 95%의 요청이 이 시간 내에 응답
 * - Mean 응답시간: 평균 응답 시간
 * - 성공률: 전체 요청 대비 성공한 요청의 비율
 * - 동시 사용자 수: 피크 타임 동시 접속자 수
 * </p>
 */
public class WhispyPortfolioSimulation extends Simulation {

    /**
     * HTTP 프로토콜 설정
     */
    HttpProtocolBuilder httpProtocol = http
            .baseUrl(GatlingConfig.BASE_URL)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .authorizationHeader(GatlingConfig.getAuthorizationHeader());

    /**
     * 현재 시간 기반 세션 데이터 생성
     */
    private String generateSleepSessionJson() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startedAt = now.minusHours(8);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return String.format("""
                {
                    "started_at": "%s",
                    "ended_at": "%s",
                    "duration_seconds": 28800
                }
                """,
                startedAt.format(formatter),
                now.format(formatter)
        );
    }

    /**
     * 시나리오 1: 아침 루틴 사용자
     * 기상 후 수면 데이터를 저장하고 통계를 확인하는 패턴
     */
    ScenarioBuilder morningRoutineScenario = scenario("아침 루틴 사용자")
            .exec(session -> session.set("sleepSessionJson", generateSleepSessionJson()))
            .exec(
                    http("수면 세션 저장")
                            .post("/sleep-sessions")
                            .body(StringBody("#{sleepSessionJson}"))
                            .check(status().is(201))
            )
            .pause(Duration.ofSeconds(2), Duration.ofSeconds(4))
            .exec(
                    http("주간 수면 통계 확인")
                            .get("/statistics/sleep?period=WEEK&date=2024-12-08")
                            .check(status().is(200))
            )
            .pause(Duration.ofSeconds(1), Duration.ofSeconds(3))
            .exec(
                    http("월간 수면 통계 비교")
                            .get("/statistics/sleep/comparison?period=MONTH&date=2024-12-08")
                            .check(status().is(200))
            );

    /**
     * 시나리오 2: 음악 탐색 사용자
     * 수면을 위한 음악을 검색하고 선택하는 패턴
     */
    ScenarioBuilder musicExplorerScenario = scenario("음악 탐색 사용자")
            .exec(
                    http("수면 음악 검색")
                            .get("/search/music?keyword=sleep&page=0&size=20")
                            .check(status().is(200))
            )
            .pause(Duration.ofSeconds(1), Duration.ofSeconds(3))
            .exec(
                    http("카테고리 변경 - SLEEP")
                            .get("/search/music/category?musicCategory=SLEEP&page=0&size=20")
                            .check(status().is(200))
            )
            .pause(Duration.ofSeconds(2), Duration.ofSeconds(4))
            .exec(
                    http("음악 상세 조회")
                            .get("/search/1")
                            .check(status().is(200))
            );

    /**
     * 시나리오 3: 파워 유저
     * 모든 기능을 적극적으로 사용하는 패턴
     */
    ScenarioBuilder powerUserScenario = scenario("파워 유저")
            .exec(
                    http("내 수면 세션 목록")
                            .get("/sleep-sessions?page=0&size=20")
                            .check(status().is(200))
            )
            .pause(Duration.ofSeconds(1))
            .exec(
                    http("일일 수면 통계")
                            .get("/statistics/sleep/daily?period=WEEK&date=2024-12-08")
                            .check(status().is(200))
            )
            .pause(Duration.ofSeconds(1))
            .exec(
                    http("명상 음악 검색")
                            .get("/search/music?keyword=meditation&page=0&size=20")
                            .check(status().is(200))
            )
            .pause(Duration.ofSeconds(2))
            .exec(session -> session.set("sleepSessionJson", generateSleepSessionJson()))
            .exec(
                    http("새 수면 세션 시작")
                            .post("/sleep-sessions")
                            .body(StringBody("#{sleepSessionJson}"))
                            .check(status().is(201))
            );

    /**
     * 시나리오 4: 통계 중심 사용자
     * 수면 통계와 분석에 집중하는 패턴
     */
    ScenarioBuilder statisticsFocusedScenario = scenario("통계 중심 사용자")
            .exec(
                    http("주간 수면 통계 1")
                            .get("/statistics/sleep?period=WEEK&date=2024-12-08")
                            .check(status().is(200))
            )
            .pause(Duration.ofMillis(500), Duration.ofSeconds(1))
            .exec(
                    http("주간 수면 통계 2")
                            .get("/statistics/sleep?period=WEEK&date=2024-12-01")
                            .check(status().is(200))
            )
            .pause(Duration.ofMillis(500), Duration.ofSeconds(1))
            .exec(
                    http("월간 수면 통계")
                            .get("/statistics/sleep?period=MONTH&date=2024-12-08")
                            .check(status().is(200))
            )
            .pause(Duration.ofSeconds(1), Duration.ofSeconds(2))
            .exec(
                    http("기간 비교 분석")
                            .get("/statistics/sleep/comparison?period=WEEK&date=2024-12-08")
                            .check(status().is(200))
            );

    /**
     * 시나리오 5: 빠른 조회 사용자
     * 간단한 확인만 하는 라이트 유저
     */
    ScenarioBuilder quickCheckScenario = scenario("빠른 조회 사용자")
            .exec(
                    http("최근 수면 기록 확인")
                            .get("/sleep-sessions?page=0&size=10")
                            .check(status().is(200))
            )
            .pause(Duration.ofSeconds(1), Duration.ofSeconds(2))
            .exec(
                    http("주간 수면 통계 확인")
                            .get("/statistics/sleep?period=WEEK&date=2024-12-08")
                            .check(status().is(200))
            );

    /**
     * 부하 테스트 설정
     * <p>
     * 포트폴리오용 종합 성능 테스트:
     * - 총 3분간 실행
     * - 5가지 사용자 패턴 동시 실행
     * - 최대 동시 사용자 수: 약 300명
     * - 예상 TPS: 100~150
     * </p>
     */
    {
        setUp(
                // 아침 루틴 사용자 (20%)
                morningRoutineScenario.injectOpen(
                        rampUsers(40).during(Duration.ofSeconds(20)),
                        constantUsersPerSec(3).during(Duration.ofSeconds(60)),
                        rampUsers(60).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol),

                // 음악 탐색 사용자 (30%)
                musicExplorerScenario.injectOpen(
                        rampUsers(60).during(Duration.ofSeconds(20)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(60)),
                        rampUsers(90).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol),

                // 파워 유저 (15%)
                powerUserScenario.injectOpen(
                        rampUsers(30).during(Duration.ofSeconds(20)),
                        constantUsersPerSec(2).during(Duration.ofSeconds(60)),
                        rampUsers(45).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol),

                // 통계 중심 사용자 (25%)
                statisticsFocusedScenario.injectOpen(
                        rampUsers(50).during(Duration.ofSeconds(20)),
                        constantUsersPerSec(4).during(Duration.ofSeconds(60)),
                        rampUsers(75).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol),

                // 빠른 조회 사용자 (10%)
                quickCheckScenario.injectOpen(
                        rampUsers(20).during(Duration.ofSeconds(20)),
                        constantUsersPerSec(2).during(Duration.ofSeconds(60)),
                        rampUsers(30).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol)
        ).assertions(
                /**
                 * 포트폴리오에 기재할 성능 기준
                 */
                // 전체 성능 기준
                global().responseTime().percentile(95.0).lt(1000),  // 95% 1초 이내
                global().responseTime().percentile(99.0).lt(2000),  // 99% 2초 이내
                global().responseTime().mean().lt(500),              // 평균 500ms 이내
                global().successfulRequests().percent().gt(99.0),    // 성공률 99% 이상

                // API별 세부 기준
                details("수면 세션 저장").responseTime().mean().lt(300),
                details("주간 수면 통계 확인").responseTime().percentile(95.0).lt(800),
                details("수면 음악 검색").responseTime().percentile(95.0).lt(600),

                // 실패율 제한
                global().failedRequests().count().lt(50L)            // 전체 실패 50건 미만
        ).maxDuration(Duration.ofMinutes(3));
    }
}
