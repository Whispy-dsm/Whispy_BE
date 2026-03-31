# Whispy_BE Architecture and Risk Analysis

Date: 2026-02-18
Scope: Read-only analysis of `src/main`, `src/test`, and core runtime configuration

## 1) Executive Summary

Whispy_BE is largely aligned with a Hexagonal (Ports and Adapters) structure in practice:

- Domain modules are split into `application/port/in`, `application/port/out`, `application/service`, `adapter/in`, `adapter/out`, and `model`
- Controllers generally depend on UseCase interfaces
- Persistence adapters generally implement port interfaces
- MapStruct is used for entity-domain mapping

However, several high-impact risks exist in security exposure and operational safety.

## 2) Architecture Map (Observed)

- Application root: `src/main/java/whispy_server/whispy`
  - `domain/*` modules (admin, announcement, auth, file, focussession, history, like, meditationsession, music, notification, payment, reason, sleepsession, soundspace, statistics, topic, user)
  - `global/*` cross-cutting modules (security, exception, config, feign, oauth, document, etc.)
- Tests root: `src/test/java/whispy_server/whispy`
  - Domain-level unit tests under `domain/*/application/service/unit`
  - Integration base support via `global/support/IntegrationTestSupport.java`

## 3) Key Findings (Severity, Evidence, Impact)

### Critical

1. **Actuator endpoints are publicly permitted in security config**
   - Evidence:
     - `src/main/java/whispy_server/whispy/global/config/security/SecurityConfig.java:95`
   - Impact:
     - If deployed without network-level restriction, `/actuator/**` may expose operational metadata and metrics publicly.

### High

2. **CORS allows any origin pattern with credentials enabled**
   - Evidence:
     - `src/main/java/whispy_server/whispy/global/config/web/WebConfig.java:35`
     - `src/main/java/whispy_server/whispy/global/config/web/WebConfig.java:38`
   - Impact:
     - `allowedOriginPatterns("*")` with `allowCredentials(true)` increases cross-origin attack surface and can lead to unsafe browser credential sharing behavior.

3. **Production/staging app logger remains DEBUG**
   - Evidence:
     - `src/main/resources/logback-spring.xml:74`
   - Impact:
     - Excessive logs, potential sensitive context leakage, and higher operational costs/noise in prod/stag.

4. **Global SQL logging enabled in base config**
   - Evidence:
     - `src/main/resources/application.yml:22`
   - Impact:
     - SQL and parameter-level details can leak internals if profile override is not strict in deployment.

5. **Flyway migrate validation disabled**
   - Evidence:
     - `src/main/resources/application.yml:44`
   - Impact:
     - Schema drift and unnoticed migration mismatch risk across environments.

### Medium

6. **One service directly depends on a repository instead of a port (hexagonal boundary leak)**
   - Evidence:
     - `src/main/java/whispy_server/whispy/domain/user/application/service/UserWithdrawalService.java:14`
     - `src/main/java/whispy_server/whispy/domain/user/application/service/UserWithdrawalService.java:30`
   - Impact:
     - Domain application layer coupling to infrastructure details reduces portability and weakens architectural consistency.

7. **Redis serializer uses broad default typing**
   - Evidence:
     - `src/main/java/whispy_server/whispy/global/config/redis/RedisConfig.java:82`
   - Impact:
     - Overly permissive polymorphic typing may increase deserialization risk and maintenance complexity.

8. **Webhook endpoint is publicly permitted without explicit source verification at controller boundary**
   - Evidence:
     - `src/main/java/whispy_server/whispy/global/config/security/SecurityConfig.java:93`
     - `src/main/java/whispy_server/whispy/domain/payment/adapter/in/web/controller/GooglePlayWebhookController.java:29`
   - Impact:
     - Increased attack surface for forged requests unless verified robustly inside downstream processing.

9. **Controller integration test coverage is sparse compared to service unit tests**
   - Evidence:
     - Example integration test exists: `src/test/java/whispy_server/whispy/domain/user/adapter/in/web/controller/integration/UserLoginIntegrationTest.java`
     - Many controllers exist under `src/main/java/whispy_server/whispy/domain/*/adapter/in/web/*Controller.java`
   - Impact:
     - API contract and security behavior regressions may slip through when only service unit tests are strong.

10. **Flaky test pattern detected (`Thread.sleep`)**
    - Evidence:
      - `src/test/java/whispy_server/whispy/domain/user/adapter/in/web/controller/integration/UserLoginIntegrationTest.java:175`
    - Impact:
      - Non-deterministic CI timing failures and slower test suite.

## 4) Consistency Check vs Intended Principles

### Mostly consistent

- Ports and adapters layering present across domains
- Domain records with validation logic present (example: focus session model)
- Manual cascade deletion for logical FK strategy implemented in services

### Inconsistent points

- Direct repository dependency in application service (`UserWithdrawalService`) should be port-mediated
- Operational defaults (actuator permitAll, broad CORS, SQL log defaults) are not aligned with hardened production posture

## 5) Test Landscape Summary

- Strong service-level unit test presence across most domains (`src/test/java/.../application/service/unit/*Test.java`)
- Integration support exists (`IntegrationTestSupport`) and at least one concrete controller integration test is present
- Blind spots remain in breadth of controller integration coverage and external-boundary behavior tests

## 6) Recommended Mitigation Order

1. Restrict `/actuator/**` and swagger exposure by environment/network policy
2. Harden CORS policy (explicit trusted origins, avoid wildcard + credentials combination)
3. Set production/staging log level defaults to INFO (or narrower debug targeting)
4. Disable global SQL logging by default and control only in local profile
5. Enable Flyway validation in non-local deployments
6. Refactor `UserWithdrawalService` to use a dedicated port for refresh-token operations
7. Replace broad Redis default typing with tighter serialization strategy
8. Expand controller integration/security tests and remove sleep-based test timing

## 7) Notes

- This phase is analysis-only (no code changes applied).
- Findings are based on repository read/search and representative file inspection.
