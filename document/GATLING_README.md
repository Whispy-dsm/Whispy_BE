# Whispy 포트폴리오용 성능 테스트 가이드

## 개요

Whispy Backend의 핵심 3개 API에 대한 성능 테스트를 위한 Gatling 시뮬레이션입니다.
포트폴리오 작성을 위한 실전 성능 지표를 수집할 수 있습니다.

## 📊 테스트 대상 API

### 1. 수면 통계 조회 API
- **엔드포인트**: `/statistics/sleep`
- **특징**: 복잡한 통계 쿼리, DB 집계 성능
- **포트폴리오 어필**: "복잡한 통계 쿼리 최적화로 95 percentile 응답시간 XXXms 달성"

### 2. 음악 검색 API
- **엔드포인트**: `/search/music`, `/search/music/category`
- **특징**: 검색 성능, 페이징 처리, 인덱싱 효과
- **포트폴리오 어필**: "초당 XXX건의 검색 요청 처리, 적절한 인덱스 설계로 응답시간 개선"

### 3. 수면 세션 API
- **엔드포인트**: `/sleep-sessions`
- **특징**: Write/Read 성능, 동시성 처리, 트랜잭션
- **포트폴리오 어필**: "동시 XXX명의 세션 저장 요청 처리, TPS XXX 달성"

## 🚀 시뮬레이션 종류

### 1. SleepStatisticsPerformanceSimulation
수면 통계 조회 API 전용 성능 테스트

**테스트 시나리오:**
- 주간/월간/연간 통계 조회
- 기간 비교 통계
- 일일 상세 통계

**실행 명령어:**
```bash
./gradlew gatlingRun-whispy_server.whispy.simulations.statistics.sleep.SleepStatisticsPerformanceSimulation
```

**예상 성능 지표:**
- 95 percentile: 1초 이내
- 평균 응답시간: 500ms 이내
- 성공률: 99% 이상

---

### 2. MusicSearchPerformanceSimulation
음악 검색 API 전용 성능 테스트

**테스트 시나리오:**
- 키워드 검색
- 카테고리별 검색
- 음악 상세 조회
- 혼합 검색 패턴 (실제 사용자 행동)

**실행 명령어:**
```bash
./gradlew gatlingRun-whispy_server.whispy.simulations.music.MusicSearchPerformanceSimulation
```

**예상 성능 지표:**
- 95 percentile: 800ms 이내
- 평균 응답시간: 400ms 이내
- 성공률: 99.5% 이상

---

### 3. SleepSessionPerformanceSimulation
수면 세션 API 전용 성능 테스트 (읽기/쓰기 균형)

**테스트 시나리오:**
- 세션 저장 (쓰기 30%)
- 세션 목록 조회 (읽기 70%)
- 세션 상세 조회
- 실제 사용자 플로우
- 세션 삭제

**실행 명령어:**
```bash
./gradlew gatlingRun-whispy_server.whispy.simulations.statistics.sleep.SleepSessionPerformanceSimulation
```

**예상 성능 지표:**
- 쓰기 평균: 300ms 이내
- 읽기 95 percentile: 500ms 이내
- 전체 성공률: 99% 이상

---

### 4. WhispyPortfolioSimulation ⭐ 추천
**3개 API를 모두 통합한 종합 성능 테스트**

**테스트 시나리오:**
- 아침 루틴 사용자 (20%): 세션 저장 → 통계 확인
- 음악 탐색 사용자 (30%): 음악 검색 → 카테고리 변경 → 상세 조회
- 파워 유저 (15%): 모든 기능 적극 사용
- 통계 중심 사용자 (25%): 다양한 기간별 통계 조회
- 빠른 조회 사용자 (10%): 간단한 확인

**실행 명령어:**
```bash
./gradlew gatlingRun-whispy_server.whispy.simulations.WhispyPortfolioSimulation
```

**예상 성능 지표:**
- 최대 동시 사용자: 약 300명
- 예상 TPS: 100~150
- 95 percentile: 1초 이내
- 평균 응답시간: 500ms 이내
- 성공률: 99% 이상

**실행 시간**: 약 3분

---

## 📋 테스트 전 준비사항

### 1. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 2. JWT 토큰 발급
인증이 필요한 시뮬레이션의 경우 JWT 토큰을 발급받아야 합니다.

**방법 1: Swagger UI 사용**
1. http://localhost:8080/swagger-ui.html 접속
2. 로그인 API로 토큰 발급
3. 시뮬레이션 파일의 `JWT_TOKEN` 상수 교체

**방법 2: curl 사용**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}'
```

**토큰 교체 위치:**
```java
private static final String JWT_TOKEN = "여기에_실제_토큰_붙여넣기";
```

### 3. 테스트 데이터 준비
다음 CSV 파일들이 `src/gatling/resources/`에 있어야 합니다:
- ✅ `sleep-statistics-data.csv` (기간 타입 및 날짜)
- ✅ `music-keywords.csv` (검색 키워드)
- ✅ `music-categories.csv` (음악 카테고리)

## 🎯 실행 방법

### 개별 시뮬레이션 실행
```bash
# 수면 통계 성능 테스트
./gradlew gatlingRun-whispy_server.whispy.simulations.statistics.sleep.SleepStatisticsPerformanceSimulation

# 음악 검색 성능 테스트
./gradlew gatlingRun-whispy_server.whispy.simulations.music.MusicSearchPerformanceSimulation

# 수면 세션 성능 테스트
./gradlew gatlingRun-whispy_server.whispy.simulations.statistics.sleep.SleepSessionPerformanceSimulation

# 통합 포트폴리오 성능 테스트 (추천)
./gradlew gatlingRun-whispy_server.whispy.simulations.WhispyPortfolioSimulation
```

### 모든 시뮬레이션 실행
```bash
./gradlew gatlingRun
```

## 📈 결과 확인

### 리포트 위치
```
build/reports/gatling/{시뮬레이션명}-{타임스탬프}/index.html
```

### 주요 성능 지표 설명

#### 1. Response Time (응답 시간)
- **Mean**: 평균 응답 시간
- **95 Percentile**: 95%의 요청이 이 시간 내에 응답
- **99 Percentile**: 99%의 요청이 이 시간 내에 응답
- **Max**: 최대 응답 시간

#### 2. TPS (Transactions Per Second)
- 초당 처리할 수 있는 트랜잭션(요청) 수
- 높을수록 좋음

#### 3. Success Rate (성공률)
- 전체 요청 대비 성공한 요청의 비율
- 99% 이상이 목표

#### 4. Request Count (요청 수)
- 총 요청 수
- OK: 성공한 요청
- KO: 실패한 요청

## 💡 포트폴리오 작성 팁

### 성능 테스트 결과 예시 작성법

```markdown
## 성능 테스트 결과

### 테스트 환경
- **도구**: Gatling 3.11.5
- **서버**: Spring Boot 3.4.5, Java 21
- **DB**: MySQL 8.x
- **캐시**: Redis

### 수면 통계 조회 API 성능
- **동시 사용자**: 최대 100명
- **총 요청 수**: 1,234건
- **평균 응답시간**: 387ms
- **95 Percentile**: 842ms
- **성공률**: 99.8%
- **TPS**: 45.2

### 음악 검색 API 성능
- **동시 사용자**: 최대 120명
- **총 요청 수**: 2,456건
- **평균 응답시간**: 298ms
- **95 Percentile**: 651ms
- **성공률**: 99.9%
- **TPS**: 78.3

### 수면 세션 API 성능 (읽기/쓰기 혼합)
- **동시 사용자**: 최대 80명
- **총 요청 수**: 1,567건
- **쓰기 평균**: 245ms
- **읽기 평균**: 321ms
- **성공률**: 99.5%
- **TPS**: 52.1

### 종합 성능 (WhispyPortfolioSimulation)
- **최대 동시 사용자**: 300명
- **총 요청 수**: 5,678건
- **평균 응답시간**: 456ms
- **95 Percentile**: 987ms
- **성공률**: 99.2%
- **TPS**: 125.4
```

### 개선 사항 어필 예시

```markdown
## 성능 최적화 작업

### 1. 데이터베이스 인덱스 최적화
- 음악 검색 쿼리에 복합 인덱스 추가
- 결과: 검색 응답시간 45% 개선 (540ms → 298ms)

### 2. 통계 쿼리 최적화
- QueryDSL을 활용한 동적 쿼리 최적화
- 불필요한 조인 제거
- 결과: 통계 조회 응답시간 30% 개선

### 3. 캐싱 전략 도입
- Redis를 활용한 통계 데이터 캐싱
- 캐시 히트율: 85%
- 결과: 평균 응답시간 50% 개선
```

## ⚙️ 시뮬레이션 커스터마이징

### 부하 조정
각 시뮬레이션의 `setUp` 메서드에서 부하를 조정할 수 있습니다:

```java
setUp(
    scenario.injectOpen(
        rampUsers(100).during(Duration.ofSeconds(30)),  // 30초 동안 100명으로 증가
        constantUsersPerSec(10).during(Duration.ofSeconds(60))  // 60초 동안 초당 10명 유지
    )
)
```

### 성능 기준 변경
`assertions`를 수정하여 성능 기준을 조정할 수 있습니다:

```java
.assertions(
    global().responseTime().percentile(95.0).lt(1000), // 95% 1초 이내
    global().responseTime().mean().lt(500),             // 평균 500ms 이내
    global().successfulRequests().percent().gt(99.0)    // 성공률 99% 이상
)
```

## 🔍 문제 해결

### JWT 토큰 만료
증상: 401 Unauthorized 에러
해결: 새로운 JWT 토큰 발급 후 시뮬레이션 파일 업데이트

### 연결 거부 (Connection Refused)
증상: Connection refused 에러
해결: 애플리케이션이 localhost:8080에서 실행 중인지 확인

### 데이터베이스 연결 오류
증상: DB 관련 에러
해결: MySQL과 Redis가 실행 중인지 확인

### 메모리 부족
증상: OutOfMemoryError
해결: Gradle JVM 옵션 조정
```bash
export GRADLE_OPTS="-Xmx4g"
./gradlew gatlingRun
```

## 📚 참고 자료

- [Gatling 공식 문서](https://gatling.io/docs/gatling/)
- [Gatling Java DSL](https://gatling.io/docs/gatling/reference/current/core/simulation/)
- [성능 테스트 Best Practices](https://gatling.io/docs/gatling/tutorials/advanced/)

## 🎓 성능 테스트 Best Practices

1. **점진적 부하 증가**: 급격한 부하보다는 rampUsers로 점진적 증가
2. **Think Time 추가**: pause로 실제 사용자의 행동 패턴 반영
3. **충분한 테스트 데이터**: CSV 파더를 활용한 다양한 테스트 데이터
4. **모니터링**: 테스트 중 서버 리소스(CPU, 메모리, DB 연결) 모니터링
5. **반복 테스트**: 여러 번 테스트하여 일관된 결과 확인
6. **Warm-up**: 첫 번째 결과는 버리고 두 번째 이후 결과 사용

## 📊 포트폴리오에 포함할 내용

✅ 테스트 환경 (서버 스펙, DB, 캐시 등)
✅ 테스트 시나리오 설명
✅ 주요 성능 지표 (TPS, 응답시간, 성공률)
✅ 성능 그래프 (Gatling 리포트 스크린샷)
✅ 병목 지점 분석
✅ 성능 개선 작업 및 결과
✅ 학습한 점 및 개선 방향
