package whispy_server.whispy.simulations.sleepsession;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import whispy_server.whispy.simulations.config.GatlingConfig;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * 수면 세션 API 성능 테스트 시뮬레이션
 *
 * 수면 세션 저장(쓰기)과 조회(읽기) 성능을 측정합니다.
 * 동시성 처리 능력과 트랜잭션 성능을 검증하기 위한 포트폴리오용 시뮬레이션입니다.
 *
 */
public class SleepSessionPerformanceSimulation extends Simulation {

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
     * 수면 세션 저장 시나리오 (쓰기 성능)
     * 동시 다발적인 세션 저장 요청 처리 능력을 측정합니다
     */
    ScenarioBuilder saveSleepSessionScenario = scenario("수면 세션 저장")
            .exec(session -> {
                String json = generateSleepSessionJson();
                return session.set("sleepSessionJson", json);
            })
            .exec(
                    http("수면 세션 저장")
                            .post("/sleep-sessions")
                            .body(StringBody("#{sleepSessionJson}"))
                            .check(status().is(201))
                            .check(jsonPath("$.id").saveAs("sessionId"))
                            .check(jsonPath("$.started_at").exists())
                            .check(jsonPath("$.ended_at").exists())
            )
            .pause(Duration.ofMillis(500), Duration.ofSeconds(2));

    /**
     * 수면 세션 목록 조회 시나리오 (읽기 성능)
     * 페이징 처리된 목록 조회 성능을 측정합니다
     */
    ScenarioBuilder getSleepSessionListScenario = scenario("수면 세션 목록 조회")
            .exec(
                    http("세션 목록 - 첫 페이지")
                            .get("/sleep-sessions?page=0&size=20&sort=createdAt,desc")
                            .check(status().is(200))
                            .check(jsonPath("$.content").exists())
                            .check(jsonPath("$.total_elements").exists())
            )
            .pause(Duration.ofMillis(300), Duration.ofSeconds(1))
            .exec(
                    http("세션 목록 - 두 번째 페이지")
                            .get("/sleep-sessions?page=1&size=20&sort=createdAt,desc")
                            .check(status().is(200))
            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(800));

    /**
     * 수면 세션 상세 조회 시나리오
     * 실제 사용자 플로우: 목록 조회 → 첫 번째 세션 클릭 → 상세 조회
     */
    ScenarioBuilder getSleepSessionDetailScenario = scenario("수면 세션 목록 후 상세 조회")
            .exec(
                    http("세션 목록 조회")
                            .get("/sleep-sessions?page=0&size=20&sort=createdAt,desc")
                            .check(status().is(200))
                            .check(jsonPath("$.content[0].id").saveAs("sessionId"))  // 첫 번째 세션 ID 추출
            )
            .pause(1, 2)  // 목록 보는 시간
            .exec(
                    http("세션 상세 조회 - ID #{sessionId}")
                            .get("/sleep-sessions/#{sessionId}")
                            .check(status().is(200))
                            .check(jsonPath("$.id").exists())
                            .check(jsonPath("$.duration_seconds").exists())
            )
            .pause(2, 4);  // 상세 정보 보는 시간

    /**
     * 실제 사용자 플로우 시나리오
     * 저장 -> 목록 조회 -> 상세 조회의 전체 흐름을 시뮬레이션합니다
     */
    ScenarioBuilder realUserFlowScenario = scenario("실제 사용자 플로우")
            .exec(session -> {
                String json = generateSleepSessionJson();
                return session.set("sleepSessionJson", json);
            })
            .exec(
                    http("1. 수면 세션 저장")
                            .post("/sleep-sessions")
                            .body(StringBody("#{sleepSessionJson}"))
                            .check(status().is(201))
                            .check(jsonPath("$.id").saveAs("newSessionId"))
            )
            .pause(Duration.ofSeconds(1), Duration.ofSeconds(3))
            .exec(
                    http("2. 내 세션 목록 확인")
                            .get("/sleep-sessions?page=0&size=20")
                            .check(status().is(200))
            )
            .pause(Duration.ofMillis(500), Duration.ofSeconds(2))
            .exec(
                    http("3. 방금 저장한 세션 상세 조회")
                            .get("/sleep-sessions/#{newSessionId}")
                            .check(status().is(200))
            );

    /**
     * 수면 세션 삭제 시나리오
     * 저장 후 삭제하는 패턴 테스트
     */
    ScenarioBuilder deleteSleepSessionScenario = scenario("수면 세션 삭제")
            .exec(session -> {
                String json = generateSleepSessionJson();
                return session.set("sleepSessionJson", json);
            })
            .exec(
                    http("세션 저장 (삭제 테스트용)")
                            .post("/sleep-sessions")
                            .body(StringBody("#{sleepSessionJson}"))
                            .check(status().is(201))
                            .check(jsonPath("$.id").saveAs("deleteTargetId"))
            )
            .pause(Duration.ofSeconds(1))
            .exec(
                    http("세션 삭제")
                            .delete("/sleep-sessions/#{deleteTargetId}")
                            .check(status().is(204))
            );

    /**
     * 부하 테스트 설정
     *
     * 읽기/쓰기 성능 균형 테스트:
     * - 읽기 : 쓰기 비율 = 7:3 (일반적인 패턴)
     * - 동시성 처리 능력 검증
     * - TPS(Transaction Per Second) 측정
     *
     */
    {
        setUp(
                // 읽기: 세션 목록 조회 (70% 비중)
                getSleepSessionListScenario.injectOpen(
                        rampUsers(70).during(Duration.ofSeconds(15)),
                        constantUsersPerSec(12).during(Duration.ofSeconds(25)),
                        rampUsers(100).during(Duration.ofSeconds(10))
                ).protocols(httpProtocol),

                // 쓰기: 세션 저장 (30% 비중)
                saveSleepSessionScenario.injectOpen(
                        rampUsers(30).during(Duration.ofSeconds(15)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(25)),
                        rampUsers(50).during(Duration.ofSeconds(10))
                ).protocols(httpProtocol),

                // 상세 조회
                getSleepSessionDetailScenario.injectOpen(
                        rampUsers(20).during(Duration.ofSeconds(15)),
                        constantUsersPerSec(3).during(Duration.ofSeconds(25))
                ).protocols(httpProtocol),

                // 실제 사용자 플로우
                realUserFlowScenario.injectOpen(
                        rampUsers(15).during(Duration.ofSeconds(15)),
                        constantUsersPerSec(2).during(Duration.ofSeconds(25))
                ).protocols(httpProtocol),

                // 삭제 시나리오 (소량)
                deleteSleepSessionScenario.injectOpen(
                        rampUsers(10).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol)
        ).assertions(
                global().responseTime().percentile(95.0).lt(1000), // 95% 요청이 1초 이내
                global().responseTime().percentile(99.0).lt(2000), // 99% 요청이 2초 이내
                global().responseTime().mean().lt(500),             // 평균 응답시간 500ms 이내
                global().successfulRequests().percent().gt(99.0),   // 성공률 99% 이상

                // 쓰기 작업 성능 기준 (더 엄격)
                details("수면 세션 저장").responseTime().mean().lt(300),

                // 읽기 작업 성능 기준
                details("세션 목록 - 첫 페이지").responseTime().percentile(95.0).lt(500)
        ).maxDuration(Duration.ofMinutes(2));
    }
}
