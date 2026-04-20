# Whispy_BE Claude Skills

Whispy 백엔드 프로젝트에 최적화된 Claude Code 스킬 모음입니다.

## 📦 설치된 스킬

### 1. Java Spring Boot Skills
**위치:** `.claude/skills/java-spring-boot/`

Java와 Spring Framework 전문가 스킬팩으로, Java 생태계 전체를 커버합니다.

**포함된 스킬:**
- `java-fundamentals` - Core Java 개념
- `java-advanced` - 고급 Java (동시성, 스트림)
- `java-spring-boot` - Spring Framework (Boot, MVC, Security, Data, Cloud)
- `java-testing` - JUnit, Mockito, 통합 테스트
- `java-maven-gradle` - Maven, Gradle 빌드 도구
- `java-jpa-hibernate` - JPA, Hibernate, DB 최적화
- `java-microservices` - 마이크로서비스 패턴
- `java-docker` - Docker, Kubernetes, CI/CD

**적합한 작업:**
- Spring Boot 3.x 애플리케이션 개발
- Spring Security + JWT 인증
- JPA/Hibernate 쿼리 최적화
- REST API 개발
- Gradle 빌드 설정

---

### 2. Spring Boot Patterns
**위치:** `.claude/skills/springboot-patterns/`

Spring Boot 아키텍처 패턴, REST API 설계, 서비스 레이어, 데이터 액세스, 캐싱, 비동기 처리, 로깅 패턴을 제공합니다.

**주요 내용:**
- REST API 구조 및 컨트롤러 패턴
- Repository 패턴 (Spring Data JPA)
- Service Layer 및 트랜잭션 관리
- DTO 및 Bean Validation
- 전역 예외 처리 (`@ControllerAdvice`)
- 캐싱 (`@Cacheable`, `@CacheEvict`)
- 비동기 처리 (`@Async`)
- Rate Limiting (Bucket4j)
- 로깅 및 모니터링

---

### 3. Spring Boot Security
**위치:** `.claude/skills/springboot-security/`

Spring Security 베스트 프랙티스: 인증/인가, 검증, CSRF, 시크릿 관리, 보안 헤더, Rate Limiting, 의존성 보안을 다룹니다.

**주요 내용:**
- JWT 토큰 인증 필터
- `@EnableMethodSecurity` 및 `@PreAuthorize`
- Input Validation (Bean Validation)
- SQL Injection 방지
- CSRF 보호 전략
- 보안 헤더 설정
- 릴리스 전 보안 체크리스트

---

### 4. Spring Boot TDD
**위치:** `.claude/skills/springboot-tdd/`

JUnit 5, Mockito, MockMvc, Testcontainers, JaCoCo를 활용한 테스트 주도 개발 가이드입니다.

**주요 내용:**
- TDD 워크플로우 (실패하는 테스트 → 구현 → 리팩터링)
- 단위 테스트 패턴 (MockitoExtension)
- 웹 레이어 테스트 (`@WebMvcTest`)
- 통합 테스트 (`@SpringBootTest`)
- 데이터 액세스 테스트 (`@DataJpaTest`)
- Testcontainers 사용
- 80%+ 커버리지 목표 (JaCoCo)

---

### 5. Spring Boot Verification
**위치:** `.claude/skills/springboot-verification/`

PR, 주요 변경사항, 배포 전 검증 루프: 빌드, 정적 분석, 테스트 + 커버리지, 보안 스캔, diff 리뷰를 수행합니다.

**검증 단계:**
1. Build (`./gradlew clean assemble -x test`)
2. Static Analysis (SpotBugs, PMD, Checkstyle)
3. Tests + Coverage (JaCoCo 80%+)
4. Security Scan (OWASP Dependency Check)
5. Lint/Format (Spotless)
6. Diff Review

---

### 6. JPA Patterns
**위치:** `.claude/skills/jpa-patterns/`

JPA/Hibernate 패턴: 엔티티 설계, 관계, 쿼리 최적화, 트랜잭션, 감사(Auditing), 인덱싱, 페이지네이션, 커넥션 풀링을 다룹니다.

**주요 내용:**
- 엔티티 설계 및 `@EntityListeners`
- N+1 문제 방지 (`JOIN FETCH`, DTO Projection)
- Repository 패턴 및 쿼리 메서드
- 트랜잭션 관리 (`@Transactional`)
- 페이지네이션 및 정렬
- HikariCP 커넥션 풀 설정
- Flyway/Liquibase 마이그레이션

---

### 7. PostgreSQL Patterns
**위치:** `.claude/skills/postgres-patterns/`

PostgreSQL 데이터베이스 패턴: 쿼리 최적화, 스키마 설계, 인덱싱, 보안. Supabase 베스트 프랙티스 기반입니다.

**주요 내용:**
- 인덱스 타입별 가이드 (B-tree, GIN, BRIN)
- 데이터 타입 권장사항
- Composite Index, Covering Index, Partial Index
- UPSERT, Cursor Pagination
- Queue Processing (`FOR UPDATE SKIP LOCKED`)
- Anti-Pattern 탐지 쿼리
- 설정 템플릿

> **참고:** 이 프로젝트는 MySQL을 사용하지만, 인덱싱 및 쿼리 최적화 원칙은 동일하게 적용됩니다.

---

### 8. Java Coding Standards
**위치:** `.claude/skills/java-coding-standards/`

Spring Boot 서비스를 위한 Java 코딩 표준: 네이밍, 불변성, Optional 사용법, 스트림, 예외, 제네릭, 프로젝트 레이아웃을 다룹니다.

**주요 내용:**
- 네이밍 컨벤션 (PascalCase, camelCase, UPPER_SNAKE_CASE)
- 불변성 패턴 (Record, final 필드)
- Optional 사용 가이드
- Stream 베스트 프랙티스
- 예외 처리 패턴
- 로깅 표준 (SLF4J)
- 코드 스멜 회피

---

### 9. Backend Patterns
**위치:** `.claude/skills/backend-patterns/`

백엔드 아키텍처 패턴, API 설계, 데이터베이스 최적화, 서버사이드 베스트 프랙티스를 다룹니다.

**주요 내용:**
- RESTful API 구조
- Repository 패턴
- Service Layer 패턴
- 미들웨어/필터 패턴
- 캐싱 전략 (Redis)
- 에러 처리 및 재시도 패턴
- 인증/인가 (JWT, RBAC)
- Rate Limiting
- 백그라운드 작업 및 큐
- 구조화된 로깅

---

## 🎯 프로젝트별 사용 가이드

### Whispy_BE 기술 스택 매칭:

| 기술 스택 | 관련 Skills |
|----------|-------------|
| **Java 21** | `java-coding-standards`, `java-spring-boot` |
| **Spring Boot 3.4.5** | `springboot-patterns`, `java-spring-boot` |
| **MySQL + JPA** | `jpa-patterns`, `postgres-patterns` (원칙 동일) |
| **Flyway** | `jpa-patterns` (마이그레이션 섹션) |
| **Redis** | `backend-patterns` (캐싱 섹션) |
| **Spring Security + JWT** | `springboot-security` |
| **Gradle** | `java-spring-boot` |
| **JUnit + Mockito** | `springboot-tdd` |
| **Hexagonal Architecture** | 프로젝트 AGENTS.md 참조 |

---

## 🚀 사용 방법

Skills는 **자동으로 활성화**됩니다. Claude가 작업 컨텍스트를 보고 적절한 skill을 자동으로 사용합니다.

---

## 📂 디렉토리 구조

```
.claude/skills/
├── backend-patterns/          # 백엔드 아키텍처 패턴
├── java-coding-standards/     # Java 코딩 표준
├── java-spring-boot/          # Java + Spring Boot 전문가 스킬팩
├── jpa-patterns/              # JPA/Hibernate 패턴
├── postgres-patterns/         # PostgreSQL 패턴 (MySQL에도 적용 가능)
├── springboot-patterns/       # Spring Boot 개발 패턴
├── springboot-security/       # Spring Security 베스트 프랙티스
├── springboot-tdd/            # Spring Boot TDD 가이드
├── springboot-verification/   # 검증 루프
└── README.md                  # 이 파일
```

---

## ⚠️ 프로젝트 규칙 우선

이 스킬들은 일반적인 가이드라인을 제공합니다. **프로젝트 특정 규칙은 항상 `AGENTS.md`를 우선**하세요.

특히 다음 규칙들은 프로젝트 고유:
- **논리적 외래키**: JPA 관계 어노테이션 사용 금지 (`@ManyToOne` 등)
- **예외 패턴**: 싱글톤 패턴 + ErrorCode enum 사용
- **헥사고날 아키텍처**: Port/Adapter 패턴 준수
- **Flyway 마이그레이션**: DDL 자동 생성 금지

---

**Last Updated:** 2026-02-04
