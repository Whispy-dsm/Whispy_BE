package whispy_server.whispy.simulations.music;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import whispy_server.whispy.simulations.config.GatlingConfig;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * 음악 검색 API 성능 테스트 시뮬레이션
 * <p>
 * 음악 제목 검색과 카테고리별 검색 성능을 측정합니다.
 * 검색 쿼리 최적화 및 인덱스 효과를 검증하기 위한 포트폴리오용 시뮬레이션입니다.
 * </p>
 */
public class MusicSearchPerformanceSimulation extends Simulation {

    /**
     * HTTP 프로토콜 설정
     */
    HttpProtocolBuilder httpProtocol = http
            .baseUrl(GatlingConfig.BASE_URL)
            .acceptHeader("application/json")
            .authorizationHeader(GatlingConfig.getAuthorizationHeader());

    /**
     * 검색 키워드 피더
     * 다양한 검색어로 테스트
     */
    FeederBuilder<String> keywordFeeder = csv("music-keywords.csv").circular();

    /**
     * 카테고리 피더
     * 다양한 음악 카테고리로 테스트
     */
    FeederBuilder<String> categoryFeeder = csv("music-categories.csv").circular();

    /**
     * 음악 제목 검색 시나리오
     * 키워드로 음악을 검색하고 페이징 처리 성능을 측정합니다
     */
    ScenarioBuilder titleSearchScenario = scenario("음악 제목 검색")
            .feed(keywordFeeder)
            .exec(
                    http("음악 검색 - #{keyword}")
                            .get("/search/music?keyword=#{keyword}&page=0&size=20")
                            .check(status().is(200))
                            .check(jsonPath("$.content").exists())
                            .check(jsonPath("$.total_elements").exists())

            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(800));

    /**
     * 카테고리별 검색 시나리오
     * 카테고리로 음악을 필터링하고 성능을 측정합니다
     */
    ScenarioBuilder categorySearchScenario = scenario("카테고리별 음악 검색")
            .feed(categoryFeeder)
            .exec(
                    http("카테고리 검색 - #{category}")
                            .get("/search/music/category?musicCategory=#{category}&page=0&size=20")
                            .check(status().is(200))
                            .check(jsonPath("$.content").exists())
                            .check(jsonPath("$.total_elements").exists())
            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(800));

    /**
     * 음악 상세 조회 시나리오
     * 실제 사용자 플로우: 검색 → 첫 번째 결과 클릭 → 상세 조회
     */
    ScenarioBuilder musicDetailScenario = scenario("음악 검색 후 상세 조회")
            .feed(keywordFeeder)
            .exec(
                    http("음악 검색")
                            .get("/search/music?keyword=#{keyword}&page=0&size=20")
                            .check(status().is(200))
                            .check(jsonPath("$.content[0].id").saveAs("musicId"))  // 첫 번째 음악 ID 추출
            )
            .pause(1, 3)  // 검색 결과 보는 시간 (1~3초 랜덤)
            .exec(
                    http("음악 상세 조회 - ID #{musicId}")
                            .get("/search/#{musicId}")
                            .check(status().is(200))
                            .check(jsonPath("$.id").exists())
                            .check(jsonPath("$.title").exists())
            )
            .pause(2, 5);  // 음악 상세 정보 보는 시간 (2~5초 랜덤)

    /**
     * 혼합 검색 시나리오
     * 실제 사용자 패턴: 키워드 검색 → 카테고리 변경 → 상세 조회
     */
    ScenarioBuilder mixedSearchScenario = scenario("혼합 검색 패턴")
            .feed(keywordFeeder)
            .exec(
                    http("키워드 검색")
                            .get("/search/music?keyword=#{keyword}&page=0&size=20")
                            .check(status().is(200))
            )
            .pause(1, 3)
            .feed(categoryFeeder)
            .exec(
                    http("카테고리 변경")
                            .get("/search/music/category?musicCategory=#{category}&page=0&size=20")
                            .check(status().is(200))
                            .check(jsonPath("$.content[0].id").optional().saveAs("selectedMusicId"))  // 첫 번째 음악 ID 추출
            )
            .pause(1, 2)
            .doIf(session -> session.contains("selectedMusicId")).then(
                    exec(
                            http("선택한 음악 상세 조회")
                                    .get("/search/#{selectedMusicId}")
                                    .check(status().is(200))
                    )
            );

    /**
     * 부하 테스트 설정
     * <p>
     * 검색 성능 측정을 위한 단계별 부하:
     * 1. Warm-up: 10초 동안 30명까지 증가
     * 2. Load: 20초 동안 초당 15명 유지
     * 3. Stress: 15초 동안 120명까지 급증
     * </p>
     */
    {
        setUp(
                // 제목 검색 (가장 빈번한 사용)
                titleSearchScenario.injectOpen(
                        rampUsers(50).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(15).during(Duration.ofSeconds(20)),
                        rampUsers(120).during(Duration.ofSeconds(15))
                ).protocols(httpProtocol),

                // 카테고리 검색
                categorySearchScenario.injectOpen(
                        rampUsers(30).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(8).during(Duration.ofSeconds(20)),
                        rampUsers(80).during(Duration.ofSeconds(15))
                ).protocols(httpProtocol),

                // 상세 조회
                musicDetailScenario.injectOpen(
                        rampUsers(20).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(20)),
                        rampUsers(50).during(Duration.ofSeconds(15))
                ).protocols(httpProtocol),

                // 혼합 패턴 (실제 사용자 행동)
                mixedSearchScenario.injectOpen(
                        rampUsers(15).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(3).during(Duration.ofSeconds(20))
                ).protocols(httpProtocol)
        ).assertions(
                global().responseTime().percentile(95.0).lt(800),  // 95% 요청이 800ms 이내
                global().responseTime().mean().lt(400),             // 평균 응답시간 400ms 이내
                global().successfulRequests().percent().gt(99.5),   // 성공률 99.5% 이상
                forAll().failedRequests().count().lt(10L)           // 전체 실패 요청 10개 미만
        ).maxDuration(Duration.ofMinutes(2));
    }
}
