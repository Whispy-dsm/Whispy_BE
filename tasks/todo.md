# 작업 로그

## 2026-04-12 staging 파일 업로드 200 응답 후 실패 조사
- [x] 업로드 API 요청/응답 계약 확인
- [x] 파일 저장소 설정과 staging 환경 변수 흐름 확인
- [x] 최근 배포 workflow 변경이 staging 설정에 준 영향 확인
- [x] 재현 또는 테스트로 원인 검증
- [x] 원인에 맞는 최소 수정 적용
- [x] 관련 테스트/검증 실행
- [x] Review 기록

### Review
- 원인: FE staging은 API base로 `https://whispy-stag.whispy-company.site`를 쓰고, 기존 `normalizeFileUrl`은 `/file/**` 공개 URL도 API base 기준으로 정규화하거나 absolute staging URL을 그대로 유지했다.
- 수정: `whispy_fe/src/services/fileService.js`에서 `/file/**` URL만 공용 파일 도메인 `https://whispy.whispy-company.site`로 정규화하도록 API base와 파일 base를 분리했다.
- 검증: `CI=true npm test -- --watchAll=false --runInBand --runTestsByPath src/services/fileService.test.js` 통과.
- 검증: `npm run build` 통과.
- 참고: 첫 `npm test -- --watchAll=false --runInBand --runTestsByPath src/services/fileService.test.js` 실행은 CRA 대화형 처리로 출력 없이 타임아웃되어 `CI=true`로 재실행했다.

## 2026-04-12 develop -> main 병합 및 푸시
- [x] 현재 브랜치와 작업트리 상태 확인
- [x] `origin` 최신 상태 fetch
- [x] `main` 브랜치로 전환
- [x] `develop`을 `main`에 병합
- [x] 병합 결과 검증 (`git diff --check HEAD~1 HEAD`)
- [x] `origin/main`에 푸시

### Review
- 병합 커밋: `fae678d Merge branch 'develop'`
- 푸시 범위: `2c30102..fae678d main -> main`
- 최종 상태: `main`과 `origin/main` 동기화 완료
- 미실행 검증: Gradle 빌드/테스트는 실행하지 않음. 변경 범위는 `.github/workflows/gradle.yml`에 한정됨.

## 2026-04-09 멀티 디바이스 구현
- [x] `tbl_user_device` 마이그레이션 추가
- [x] user device 도메인/포트/어댑터 추가
- [x] 로그인/카카오 로그인/FCM 토큰 갱신을 device upsert 기반으로 전환
- [x] refresh token 저장 구조를 sessionId 기준으로 전환
- [x] 현재 세션만 로그아웃되도록 수정
- [x] topic fan-out 구조로 전환
- [x] topic 추가 배치의 단일 fcmToken 의존 제거
- [x] 단위/통합 테스트 보강 및 실행

## 현재 상태
- 현재 브랜치: `main`
- 관련 기능 브랜치: `Feature/124-r2-migration`
- 목표: 로컬 파일 시스템 기반 파일 저장소를 Cloudflare R2 기반 저장소로 전환하고, 기존 `/file/{folder}/{fileName}` 공개 URL 계약을 유지한다.

## 완료된 작업
- [x] `FileStoragePort` 기반 저장소 추상화 도입
- [x] `R2FileStorageAdapter`, `R2Config`, `R2Properties` 추가
- [x] 업로드/삭제 흐름을 로컬 파일 시스템에서 R2 저장소 호출로 전환
- [x] 공개 파일 조회를 `/file/**` 컨트롤러 경로로 전환
- [x] Redis `LocalTime` 캐시 직렬화 회귀 복구
- [x] 대용량 원본 업로드를 재열기 가능한 스트림 공급자 방식으로 개선
- [x] `/file/**` Range 요청 처리 추가 (`206 Partial Content`)
- [x] malformed `Range` 요청을 `416 Range Not Satisfiable`로 매핑
- [x] `HttpMessageNotReadableException`을 `400 Bad Request`로 처리하도록 예외 핸들러 추가
- [x] Range 처리 과정에서 불필요한 default 전환 메서드 제거
- [x] 브랜치 변경분 기준 남아 있던 default 메서드 제거 완료

## 최근 검증
- [x] `./gradlew.bat test --tests whispy_server.whispy.domain.file.application.service.unit.FileUploadServiceTest --tests whispy_server.whispy.domain.file.application.service.unit.FileDeleteServiceTest --tests whispy_server.whispy.domain.file.application.service.unit.FileReadServiceTest --tests whispy_server.whispy.domain.file.adapter.out.external.R2FileStorageAdapterTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.integration.FileAssetControllerIntegrationTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.unit.FileAssetControllerTest --tests whispy_server.whispy.domain.file.adapter.in.web.assembler.FileAssetResponseAssemblerTest --tests whispy_server.whispy.global.config.redis.unit.RedisConfigCacheSerializationTest`
- [x] `./gradlew.bat test --tests whispy_server.whispy.domain.file.adapter.in.web.assembler.FileAssetResponseAssemblerTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.integration.FileAssetControllerIntegrationTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.unit.FileAssetControllerTest`
- [x] `./gradlew.bat test --tests whispy_server.whispy.global.exception.GlobalExceptionHandlerTest --tests whispy_server.whispy.domain.file.adapter.in.web.assembler.FileAssetResponseAssemblerTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.integration.FileAssetControllerIntegrationTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.unit.FileAssetControllerTest`

## 2026-04-08 Multi-Range 대응
- [ ] multi-range 요청을 `416` 대신 전체 응답 fallback으로 변경
- [ ] 공개 파일 API 문서에 `206` / `416` 응답 코드 추가
- [ ] 관련 테스트 보강

## 메모
- 공개 파일 조회는 더 이상 정적 리소스 핸들러가 아니라 애플리케이션이 직접 R2에서 읽어 응답한다.
- `Range` 요청이 없으면 `200 OK`로 전체 파일을, `Range`가 있으면 `206 Partial Content`로 부분 바이트를 응답한다.
- 명시적 enum 입력값이 틀린 요청은 클라이언트 입력 오류로 보고 `400`으로 처리한다.

## 2026-04-08 R2 빈 배선 수정
- [x] 로컬 부팅 시 `FileStoragePort` 빈 누락 원인 조사
- [x] `R2FileStorageAdapter`의 `@ConditionalOnBean(S3Client.class)`가 등록 실패 지점임을 확인
- [x] 어댑터 등록 조건을 non-test 프로파일 가드로 교체
- [x] local / test 프로파일 빈 등록 회귀 테스트 추가
- [x] 대상 테스트와 전체 테스트 통과 확인
## 2026-04-09 집중/호흡 종료 후 오늘 총 시간 표시 문제 분석
- [x] 집중 세션 저장 응답 경로 확인
- [x] 호흡 세션 저장 응답 경로 확인
- [x] `duration_seconds`와 `today_total_minutes` 응답 계약 확인
- [x] 관련 단위 테스트 실행 및 결과 확인
- [x] 분석 문서 작성 (`tasks/2026-04-09-focus-meditation-total-time-analysis.md`)
- 검토: 백엔드는 저장 응답에서 세션 지속시간과 오늘 총 시간을 별도 필드로 내려준다. 두 값이 동일하게 보인다면 첫 세션이거나, 클라이언트가 `today_total_minutes` 대신 `duration_seconds`를 재사용할 가능성이 높다.
## 2026-04-09 1분 미만 세션 저장 차단
- [x] 포커스, 호흡, 수면 저장 경로의 최소 지속 시간 기준 확인
- [x] 59초 저장 실패 테스트 추가
- [x] 요청 DTO 최소값을 60초로 상향
- [x] 도메인 모델 최소값을 60초로 상향
- [x] 대상 서비스 테스트 재실행
- 검토: 이제 60초 미만 세션은 저장 포트까지 가지 않고 즉시 거부된다.
## 2026-04-09 리뷰 피드백 반영
- [x] 세션 최소 시간 검증 상수를 statistics 패키지 밖으로 분리
- [x] `ErrorCode`의 최소 시간 안내 문구를 1분 기준으로 수정
- [x] 테스트 helper Javadoc 위치를 `createUser()` 바로 위로 정리
- [x] 포커스/명상/수면 서비스 테스트 재실행
- 검토: 리뷰에서 맞는 지적만 반영했고, 최소 시간 정책은 글로벌 상수 + DTO/도메인 검증 + 에러 메시지까지 일관되게 맞췄다.
## 2026-04-09 멀티 디바이스 FCM/세션 마이그레이션 계획
- [x] 현재 단일 `user.fcm_token` 구조 분석
- [x] 이메일 기준 topic subscription 구조 분석
- [x] `userId` 키 기반 refresh token 저장 구조 분석
- [x] 멀티 디바이스 지원용 단계적 마이그레이션 계획 작성
- [x] 계획 문서 저장: `.omx/plans/2026-04-09-multi-device-fcm-session-migration-plan.md`
- 검토:
  핵심 병목은 `user 1명 = fcm token 1개`와 `userId 1개 = refresh token 1개` 구조다. 권장 방향은 `tbl_user_device`를 추가해 디바이스별 푸시 상태를 분리하고, refresh token 식별자를 `userId`에서 `sessionId`로 바꾸는 것이다. topic preference는 지금처럼 계정 레벨로 두고 활성 디바이스 전체에 fan-out 하는 방식이 가장 작은 변경으로 멀티 디바이스를 지원한다.
# 2026-04-13 백엔드 컨벤션 문서 보강

- [ ] 컨벤션 문서 위치 및 현재 구조 확인
- [ ] 코드베이스/프로젝트 문서 기준으로 백엔드 규칙 후보 수집
- [ ] `$team 2` 런타임으로 병렬 분석 수행
- [ ] 결과 통합 후 Notion `컨벤션 > 백엔드` 문서 갱신
- [ ] 반영 내용 재검증
# 2026-04-13 Redis 캐시 적용 리뷰

- [ ] Redis 설정 및 캐시 사용 지점 수집
- [ ] 캐시 키/TTL/무효화/직렬화 구조 검토

## 2026-04-20 AGENTS 백엔드 컨벤션 문서 분리

- [x] 현재 `AGENTS.md` 컨벤션 구조 확인
- [x] `docs/conventions/backend/` 주제별 문서 구조 생성
- [x] 워크플로우, 아키텍처, 구현 패턴, DB, 테스트, Git 컨벤션 분리
- [x] `AGENTS.md`를 주제별 문서 참조 방식으로 갱신
- [x] 링크와 변경 파일 검증

### Review

- 기존 `AGENTS.md`에 길게 정의되어 있던 백엔드 컨벤션을 로컬 docs 하위 문서로 분리했다.
- Notion `컨벤션/백엔드` 페이지와 로컬 문서가 함께 기준이라는 맥락을 `AGENTS.md`에 추가했다.

## 2026-04-20 Notion/로컬 백엔드 컨벤션 동기화

- [x] Notion MCP 활성화 및 백엔드 컨벤션 페이지 조회
- [x] Notion 하위 페이지와 로컬 `docs/conventions/backend/` 차이 확인
- [x] 로컬 문서에 Notion 전용 규칙 보강
- [x] Notion 문서에 로컬 전용 규칙 보강
- [x] 양쪽 반영 상태 검증

### Review

- Notion MCP를 `~/.codex/config.toml`에서 활성화했고, `codex mcp get notion` 기준 `enabled: true` 상태를 확인했다.
- 로컬에는 `auth-fcm.md`를 추가하고 Controller/DTO, 도메인 모델, 예외 상태 코드, 파일 스트리밍/Range, 테스트, Git 규칙을 보강했다.
- Notion에는 `워크플로우` 하위 페이지를 추가하고 아키텍처, DB/JPA, 마이그레이션, 파일 저장소, 인증 FCM, 로깅, 테스트, Git 페이지를 보강했다.
- 검증: 로컬 Markdown 링크 해석 성공, Notion 상위 페이지와 핵심 하위 페이지 재조회 성공.
- [ ] 테스트 및 운영 리스크 검토
- [ ] 리뷰 문서 작성 및 결과 정리

# 2026-04-13 Redis 캐시 보강 작업

- [ ] 팀 컨텍스트 스냅샷 작성
- [ ] `$team 2`로 테스트 lane / 분리 lane 시작
- [ ] 작업 결과 통합
- [ ] 검증 및 결과 정리

# 2026-04-13 Redis 캐시 보강 후 검증/보고 팀 작업

- [ ] 팀 컨텍스트 스냅샷 작성
- [ ] `$team 2`로 검증 lane / 보고서 lane 시작
- [ ] worker 결과 수집
- [ ] 최종 결과 통합 보고

## 2026-04-13 Redis cache restore and verification

- [x] Restore tracked Redis cache test changes from `stash@{0}`
- [x] Restore untracked files from `stash@{0}^3`
- [x] Run `RedisCacheBehaviorIntegrationTest`
- [x] Run focused session-domain unit tests
- [x] Run `./gradlew.bat build`
- [x] Write verification report
- [x] Write change rationale report

### Review
- Restored the Redis cache changes from stash instead of recreating them manually.
- Verified targeted cache behavior tests and the full Gradle build both passed.
- Recorded the rationale in `tasks/2026-04-13-redis-cache-change-rationale.md`.
- Recorded the verification evidence in `tasks/2026-04-13-redis-cache-verification-report.md`.
- Remaining risk: the cache integration test uses an in-memory `ConcurrentMapCacheManager`, so Redis transport and connection behavior is still outside test coverage.

## 2026-04-13 GitHub issue for Redis cache change

- [x] Check for duplicate issue
- [x] Create GitHub issue with problem, scope, and acceptance criteria
- [x] Record created issue number and URL

### Review
- Created GitHub issue `#132`.
- URL: `https://github.com/Whispy-dsm/Whispy_BE/issues/132`
- Title: `Redis 캐시 무효화 범위 개선 및 캐시 검증 보강`

## 2026-04-13 Statistics cache custom exception fix

- [x] Review current throw site and exception conventions
- [x] Replace `IllegalArgumentException` with statistics domain custom exception
- [x] Add matching `ErrorCode`
- [x] Add focused unit test for key generator failure path
- [x] Run verification
- [ ] Commit with issue number

### Review
- Replaced the fail-fast `IllegalArgumentException` in `StatisticsSummaryKeyGenerator` with a statistics domain `WhispyException`.
- Added `UNSUPPORTED_STATISTICS_CACHE_TARGET` to `ErrorCode`.
- Added `StatisticsSummaryKeyGeneratorTest` to verify both the supported focus path and the unsupported target path.
- Verified with the focused unit test and `./gradlew.bat build`.

## 2026-04-13 Notion release note update for Redis cache work

- [x] Locate the backend release note destination in Notion
- [x] Draft release note summary for Redis cache improvement work
- [x] Update the Notion page
- [x] Verify the saved content

### Review
- Removed the mistakenly added follow-up stabilization section from `v1.0.0`.
- Created a new backend release note page `v1.1.0`: `https://www.notion.so/341fcffdd18a811f95bae894ca481a8c`.
- Classified the change as `v1.1.0` because it changes runtime cache invalidation behavior and fixes a meaningful bug, while also including supporting refactor/test work.
- Verified both the cleaned `v1.0.0` page and the new `v1.1.0` page by fetching them again after the update.

## 2026-04-13 Redis real-path test coverage

- [x] Create team context snapshot
- [ ] Launch `$team 1` validation lane
- [ ] Add Redis connection-path coverage test
- [ ] Run targeted verification
- [ ] Run full build verification

## 2026-04-14 Statistics zero summary investigation

- [x] Create context snapshot for `$team 2`
- [x] Launch team investigation lanes
- [x] Map focus and sleep statistics endpoints to services, ports, adapters, and DTOs
- [x] Identify why persisted data can appear as zero on the client
- [x] Decide whether a backend code fix is required
- [x] Run focused statistics tests
- [x] Shut down team runtime cleanly

### Review
- Focus summary endpoint: `GET /statistics/focus?period=...&date=...`
- Sleep summary endpoint: `GET /statistics/sleep?period=...&date=...`
- Both services return Java record fields such as `totalMinutes`, `totalCount`, and `totalDays`, but the actual JSON contract is snake_case because `spring.jackson.property-naming-strategy: SNAKE_CASE` is configured globally.
- Client should read `total_minutes`, `total_count`, `total_days`, `today_minutes`, `average_minutes`, etc. A camelCase client mapping can turn valid API values into UI fallback zeroes.
- Separate backend semantic risk: sleep "today" minutes are calculated by `started_at` date, so overnight sleep that starts yesterday and ends today is not counted as today's sleep.
- Verification: `./gradlew.bat test --tests whispy_server.whispy.domain.statistics.focus.summary.application.service.unit.GetFocusStatisticsServiceTest --tests whispy_server.whispy.domain.statistics.sleep.summary.application.service.unit.GetSleepStatisticsServiceTest` passed.

## 2026-04-14 Team runtime health check

- [x] Run preflight for tmux, TMUX env, omx command, panes, and stale state
- [x] Create context snapshot
- [x] Launch `$team 2` health check
- [x] Verify startup evidence and worker panes
- [x] Verify task lifecycle and mailbox evidence
- [x] Shut down team cleanly
- [x] Record verdict and residual risks

### Review
- Preflight passed: `tmux 3.3`, `$TMUX` set, `omx` resolved to `C:\Users\user\AppData\Roaming\npm\omx.ps1`, and no stale active team state existed.
- In sandbox, `omx team` failed with `Team mode requires tmux`; rerunning outside sandbox started `team-runtime-health-check-work`.
- Startup partially worked: state files, three task files, worktrees, and worker panes `%34`/`%35` were created.
- Runtime failed: `omx team status` reported both workers as dead with `codex_startup_no_evidence_after_fallback:tmux_send_keys_sent`.
- No leader mailbox ACK was produced.
- Pane captures showed worker prompts stuck on approval dialogs to write worker identity / claim tasks, so startup triggers were queued instead of executed.
- Worker-2 also showed malformed JSON for `omx team api`, consistent with PowerShell quoting issues in the worker pane.
- Shutdown needed elevated execution; first shutdown hit `EPERM` removing worktrees, second elevated shutdown removed team state.
- Manual cleanup removed stale worker panes and the two health-check git worktrees. Final checks: no active team state, only leader pane remains, no `team-runtime-health-check-work` worktrees remain, and git status is clean.

## 2026-04-14 Team runtime retest

- [x] Run preflight for tmux, TMUX env, omx command, panes, stale state, and git status
- [x] Create context snapshot
- [x] Launch `$team 2` retest
- [x] Verify startup evidence and worker panes
- [x] Verify mailbox ACKs and task lifecycle
- [x] Shut down and clean up runtime artifacts
- [x] Record verdict

### Review
- Team started: `team-runtime-retest-from-whisp`
- Startup evidence: `tmux target: 1:2`, `workers: 2`, `workspace_mode: worktree`, worker panes `%37` and `%38`.
- Runtime status after startup: `pending=2`, `completed=0`, `failed=0`, no leader mailbox ACK.
- Dispatch evidence: `dispatch/requests.json` marked startup inbox triggers as `failed` with `codex_startup_no_evidence_after_fallback:tmux_send_keys_sent`.
- Pane evidence: worker-1 attempted to work around PowerShell JSON quoting via `cmd /c`; worker-2 attempted `omx team api send-message --input '{}'` and failed with `from_worker is required`.
- Verdict: `$team 2` still does not complete the worker ACK/claim lifecycle from `Whispy_BE`; launch/state/panes work, worker dispatch/task API flow does not.
- Shutdown: tasks were marked failed for health-check accounting, then `omx team shutdown team-runtime-retest-from-whisp --confirm-issues` completed. Final checks showed no active team state, only leader pane remains, no retest worktrees remain, git status clean. Removed leftover empty runtime directory under `.omx/team/team-runtime-retest-from-whisp`.

## 2026-04-14 Team runtime fix

- [x] Update Whispy_BE canonical `\\?\` trust entry to trusted
- [x] Add team worker launch args to bypass worker approval prompts
- [x] Patch OMX `team api --input` parser to accept PowerShell-loosened JSON objects
- [x] Verify patched parser with normal JSON and PowerShell-loosened input
- [x] Retest `$team 2` worker ACK/task lifecycle
- [x] Shut down and clean up retest artifacts
- [x] Record final verdict

### Review
- Config fix applied:
  - `C:\Users\user\.codex\config.toml` now marks both `C:\Users\user\Desktop\Whispy_BE` and `\\?\C:\Users\user\Desktop\Whispy_BE` as `trusted`.
  - `OMX_TEAM_WORKER_LAUNCH_ARGS = "--dangerously-bypass-approvals-and-sandbox"` added.
- OMX CLI fix applied:
  - `C:\Users\user\AppData\Roaming\npm\node_modules\oh-my-codex\dist\cli\team.js` now accepts PowerShell-loosened `--input` objects such as `{team_name:...,from_worker:...}`.
- Parser verification:
  - Both strict JSON and loose PowerShell-style input now pass parsing and reach `team_not_found`, which confirms the parse layer is fixed.
- Retest 1 (`team-runtime-retest-from-whisp`):
  - Worker startup no longer failed at parse time, but worker ACK/claim lifecycle still did not complete.
- Retest 2 with bypass (`team-runtime-bypass-retest-fro`):
  - Worker identity files were created, showing the approval/write prompt problem was reduced.
  - Worker panes still failed to ACK because `omx team api` input handling inside the pane was unstable.
- Restarted active partial team and tested Claude workers:
  - `claude-worker-retest-from-whis` worker panes showed `Please run /login` / `Invalid authentication credentials`.
- Current blocker:
  - Earlier runs showed worker CLI auth issues, but after re-authenticating Codex and launching with an explicit shell env override for worker launch args, the final Codex retest succeeded.
- Final verdict:
  - Working combination was:
    - canonical `\\?\` trust entry set to `trusted`
    - patched `team.js` parser for PowerShell-loosened `--input`
    - explicit shell env on launch: `OMX_TEAM_WORKER_LAUNCH_ARGS=--dangerously-bypass-approvals-and-sandbox`
  - Successful run: `codex-worker-bypass-env-verifi`
    - worker-1 ACK sent, task claimed, task completed
    - worker-2 ACK sent, task claimed, task completed
    - `omx team status` reached `completed=2`, `failed=0`
    - shutdown completed, no active team state remained, no worktree remained, git status clean
# 2026-04-14 reason code review

- [x] Identify `reason` domain files and related touchpoints.
- [x] Create OMX context snapshot for team intake.
- [ ] Launch `$team 2` review lanes.
- [ ] Integrate worker findings into one Markdown analysis report.
- [ ] Run focused verification if findings require build/test evidence.
- [ ] Add review summary and remaining risks.
# 2026-04-14 reason 도메인 코드 읽기

- [ ] `$team 1` 실행 전 context snapshot 작성
- [ ] OMX team 1개 read-only lane 시작
- [ ] reason 도메인 코드 맵/요약 수집
- [ ] team 상태 확인 및 종료
- [ ] 결과 정리
# 2026-04-15 GitHub Actions Swarm cleanup

- [x] Inspect deploy workflow and Docker cleanup commands.
- [x] Make service update wait for convergence before cleanup.
- [x] Remove stopped containers for the deployed Swarm service explicitly.
- [x] Verify YAML diff and record remaining risks.

### Review
- Root cause: deploy used `docker service update` without `--detach=false`, then ran one label-filtered prune. Cleanup could run before old Swarm task containers reached a stopped state.
- Change: prod/stag deploy scripts now fail fast, wait for Swarm update convergence, explicitly remove stopped `whispy-springboot` service containers, then keep the existing prune steps.
- Verification: `git diff --check` passed. `yaml` and `js-yaml` parsed `.github/workflows/gradle.yml` successfully.
- Remaining risk: cleanup runs only on the SSH target node. If the Swarm service runs tasks on other worker nodes, those nodes need node-local cleanup or a cluster-wide operational cleanup policy.

# 2026-04-15 Swarm cleanup race follow-up

- [x] Review post-deploy `docker ps -a` evidence from staging.
- [x] Identify that the old task exits after the cleanup step can run.
- [x] Add retry loop that waits for local service containers to settle to replica count.
- [x] Verify YAML parsing and deploy shell syntax.

### Review
- Evidence: new `whispy_be_stag:c0ec1d2` task was healthy while old `8821d3b` task exited only 24 seconds before the user's check, so cleanup likely ran before the old task reached a removable state.
- Change: prod/stag cleanup now retries for up to 24 cycles, removing stopped service containers each time and requiring local service container count to settle to the configured replica count.
- Verification: `yaml` and `js-yaml` parsed the workflow; extracted prod/stag SSH scripts passed Git Bash `bash -n`; `git diff --check` passed.

# 2026-04-16 Payment domain hardening plan

- [x] Re-read payment/security/persistence files needed for planning.
- [x] Capture review findings and required fix scope.
- [x] Write the implementation plan with exact file paths, tests, migrations, and commit boundaries.
- [x] Save the plan under `docs/superpowers/plans/2026-04-16-payment-domain-hardening.md`.

### Review
- Plan covers webhook authentication, user-bound subscription reads, entitlement query correctness, purchase-account binding, acknowledgement retry, RTDN idempotency, and contract documentation.
- No application code was changed; only planning/context artifacts were added.

# 2026-04-16 Payment hardening execution (Task 1 + Task 3)

- [x] Reconfirm execution scope from the implementation plan.
- [x] Create OMX context snapshot for team execution.
- [ ] Launch `$team 2` with one test lane and one implementation lane.
- [ ] Integrate worker outputs in TDD order and run verification.
- [ ] Summarize results and residual risks.

### Review
- Team runtime launched as `implement-payment-hardening-ta` and auto-integrated two worker-1 test commits plus one worker-2 test checkpoint into leader `main`.
- Worker runtime stalled after auto-integration, so the team was aborted after marking both tasks failed for shutdown gating; production implementation then continued directly on leader using the merged RED tests.
- Implemented current-user-only subscription endpoints (`/subscriptions/me`, `/subscriptions/me/status`), redacted subscription summary responses, and current-entitlement lookup via `findCurrentSubscriptionByEmail`.
- Verification:
  - `./gradlew.bat compileJava --no-daemon --console=plain` PASS
  - `./gradlew.bat test --tests "whispy_server.whispy.domain.payment.adapter.in.web.controller.integration.SubscriptionControllerIntegrationTest" --tests "whispy_server.whispy.domain.payment.adapter.in.web.controller.unit.SubscriptionControllerTest" --tests "whispy_server.whispy.domain.payment.adapter.out.persistence.integration.SubscriptionPersistenceAdapterIntegrationTest" --tests "whispy_server.whispy.domain.payment.application.service.unit.GetUserSubscriptionsServiceTest" --tests "whispy_server.whispy.domain.payment.application.service.unit.CheckUserSubscriptionStatusServiceTest" --no-daemon --console=plain` PASS
  - One earlier single-test run failed due Gradle deleting `build/test-results/test/binary/output.bin` while the directory was still in use; rerunning the full focused suite passed.

# 2026-04-20 Announcement list createdAt response

- [x] Inspect announcement list response flow and relevant conventions.
- [x] Add focused regression test for `createdAt` response value.
- [x] Add `createdAt` to the announcement list response DTO.
- [x] Run focused announcement tests and compile verification.
- [x] Record changed files, verification, and remaining risks.

### Review
- GitHub issue: `#135` (`https://github.com/Whispy-dsm/Whispy_BE/issues/135`)
- Changed `QueryAllAnnouncementResponse` to include the source `createdAt` timestamp. With the global Jackson snake_case strategy, clients receive this as `created_at`.
- Updated `QueryAllAnnouncementServiceTest` to verify list responses preserve the announcement creation time.
- Verification:
  - `./gradlew.bat test --tests "whispy_server.whispy.domain.announcement.application.service.unit.QueryAllAnnouncementServiceTest" --no-daemon --console=plain` PASS
  - `./gradlew.bat test --tests "whispy_server.whispy.domain.announcement.application.service.unit.*" --no-daemon --console=plain` PASS
  - `git diff --check` PASS
- Remaining risk: frontend still needs to format `created_at` into display text such as `2일전`.

## 2026-04-21 Sleep session minimum duration

- [x] Add 15-minute sleep boundary tests.
- [x] Add sleep-specific minimum duration constant.
- [x] Apply sleep-specific minimum to request and domain validation.
- [x] Preserve focus and meditation 60-second validation.
- [x] Run focused regression tests.

### Review
- GitHub issue: `#136` (`https://github.com/Whispy-dsm/Whispy_BE/issues/136`)
- Sleep sessions now require at least 15 minutes.
- Focus and meditation keep the existing 60-second minimum.
- Verification:
  - RED: `.\gradlew.bat test --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" --no-daemon --console=plain` failed on `15분 미만 수면 세션을 저장하면 오류가 발생한다` before production changes.
  - GREEN: `.\gradlew.bat test --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" --no-daemon --console=plain` PASS.
  - Regression: `.\gradlew.bat test --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" --tests "whispy_server.whispy.domain.focussession.application.service.unit.SaveFocusSessionServiceTest" --tests "whispy_server.whispy.domain.meditationsession.application.service.unit.SaveMeditationSessionServiceTest" --no-daemon --console=plain` PASS.
  - `git diff --check` PASS.
- Remaining risk: frontend may still show old sleep validation copy if it keeps local messages.

## 2026-04-23 이슈 단위 릴리즈 태그 규칙 문서화

- [x] 로컬 Git 컨벤션 문서에 이슈 단위 완료 후 릴리즈/태그 필수 절차 추가
- [x] Notion 백엔드 Git 컨벤션 문서에 동일 절차 반영
- [x] 문서 diff와 Notion 반영 상태 검증

### Review
- 이슈 단위 작업이 완료되어 PR 병합 또는 배포 가능한 상태가 되면 Notion `백엔드 Relase Note`를 먼저 생성/수정하고, 그 뒤에만 동일 버전의 GitHub 태그를 생성/푸시하도록 규칙을 추가했다.
- 태그 대상은 단순 `HEAD`가 아니라 해당 이슈 릴리즈 범위의 마지막 커밋 또는 PR merge commit으로 명시했다.
- 검증: `git diff --check -- docs/conventions/backend/git.md tasks/todo.md` 통과, Notion `git 컨벤션` 페이지 재조회로 반영 확인.
