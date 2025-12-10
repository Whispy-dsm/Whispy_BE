package whispy_server.whispy.simulations.statistics.sleep;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import whispy_server.whispy.simulations.config.GatlingConfig;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * 수면 통계 조회 API 성능 테스트 시뮬레이션
 * <p>
 * 복잡한 통계 쿼리 성능을 측정하여 포트폴리오용 성능 지표를 수집합니다.
 * 주간/월간/연간 통계, 기간 비교, 일일 상세 통계 조회 성능을 검증합니다.
 * </p>
 */
public class SleepStatisticsPerformanceSimulation extends Simulation {

    /**
     * HTTP 프로토콜 설정
     */
    HttpProtocolBuilder httpProtocol = http
            .baseUrl(GatlingConfig.BASE_URL)
            .acceptHeader("application/json")
            .authorizationHeader(GatlingConfig.getAuthorizationHeader());

    /**
     * 테스트 데이터 피더
     * 다양한 기간 타입과 날짜로 테스트
     */
    FeederBuilder<String> periodFeeder = csv("sleep-statistics-data.csv").circular();

    /**
     * 수면 통계 조회 시나리오
     * 주간/월간/연간 통계를 조회합니다
     */
    ScenarioBuilder statisticsScenario = scenario("수면 통계 조회")
            .feed(periodFeeder)
            .exec(
                    http("수면 통계")
                            .get("/statistics/sleep?period=#{period}&date=#{date}")
                            .check(status().is(200))
                            .check(jsonPath("$.total_minutes").exists())
            )
            .pause(Duration.ofMillis(100), Duration.ofMillis(500));

    /**
     * 수면 기간 비교 시나리오
     * 이전 기간과의 비교 통계를 조회합니다
     */
    ScenarioBuilder comparisonScenario = scenario("수면 기간 비교 조회")
            .feed(periodFeeder)
            .exec(
                    http("수면 기간 비교")
                            .get("/statistics/sleep/comparison?period=#{period}&date=#{date}")
                            .check(status().is(200))
                            .check(jsonPath("$.current_period_minutes").exists())
                            .check(jsonPath("$.previous_period_minutes").exists())
            )
            .pause(Duration.ofMillis(100), Duration.ofMillis(500));

    /**
     * 일일 수면 통계 시나리오
     * 기간별 일일 상세 통계를 조회합니다
     * WEEK/MONTH는 dailyData, YEAR는 monthlyData 반환
     */
    ScenarioBuilder dailyScenario = scenario("일일 수면 통계 조회")
            .feed(periodFeeder)
            .exec(
                    http("일일 수면 통계")
                            .get("/statistics/sleep/daily?period=#{period}&date=#{date}")
                            .check(status().is(200))
                            // period에 따라 dailyData 또는 monthlyData 중 하나는 반드시 존재
            )
            .pause(Duration.ofMillis(100), Duration.ofMillis(500));

    /**
     * 부하 테스트 설정
     * <p>
     * 포트폴리오용 성능 지표 수집을 위한 단계별 부하 증가:
     * 1. Ramp-up: 10초 동안 50명까지 증가
     * 2. Steady: 30초 동안 초당 10명 유지
     * 3. Peak: 20초 동안 100명까지 급증
     * </p>
     */
    {
        setUp(
                // 수면 통계 조회 (가장 많은 부하)
                statisticsScenario.injectOpen(
                        rampUsers(50).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10).during(Duration.ofSeconds(30)),
                        rampUsers(100).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol),

                // 기간 비교 조회
                comparisonScenario.injectOpen(
                        rampUsers(30).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(30)),
                        rampUsers(60).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol),

                // 일일 상세 통계 조회
                dailyScenario.injectOpen(
                        rampUsers(20).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(3).during(Duration.ofSeconds(30)),
                        rampUsers(40).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol)
        ).assertions(
                // 포트폴리오용 성능 기준
                global().responseTime().percentile(95.0).lt(1000), // 95% 요청이 1초 이내
                global().responseTime().mean().lt(500),             // 평균 응답시간 500ms 이내
                global().successfulRequests().percent().gt(99.0)    // 성공률 99% 이상
        ).maxDuration(Duration.ofMinutes(2));
    }
}
