# Backend Database Convention

## Flyway 마이그레이션

DB 스키마 변경이 필요하면 반드시 Flyway 마이그레이션 파일을 생성합니다.

위치:

```text
src/main/resources/db/migration/
```

네이밍 컨벤션:

```text
V{버전}__{설명}.sql
```

규칙:

- 버전은 `V1`, `V2`, `V3`처럼 순차 증가합니다.
- 버전과 설명은 언더스코어 2개(`__`)로 구분합니다.
- 설명은 영문으로 작성합니다.
- 설명은 snake_case 또는 PascalCase를 사용합니다.

예시:

```text
V1__Add_video_url_to_music.sql
V2__rename_batch_tables_to_upper.sql
V3__rename_to_default_batch.sql
V4__change_admin_id_to_bigint.sql
V5__Add_artist_and_description_to_music.sql
```

## 마이그레이션 작성 예시

```sql
ALTER TABLE tbl_focus_session
    ADD INDEX idx_user_started (user_id, started_at);

ALTER TABLE tbl_user
    ADD COLUMN fcm_token VARCHAR(255) NULL COMMENT 'FCM 토큰';
```

## Flyway 중요 규칙

1. 이미 적용된 마이그레이션 파일은 수정하지 않습니다.
2. Flyway는 자동 롤백을 지원하지 않으므로 실패 시 수동 복구가 필요합니다.
3. 마이그레이션은 버전 순서대로 실행되므로 누락 없이 순차 증가시킵니다.
4. 통합 테스트에서는 `application-test.yml`에서 `flyway.enabled: false`로 Flyway를 비활성화합니다.

## JPA DDL 설정

Flyway와 함께 사용할 때는 운영 환경에서 `ddl-auto: validate`를 권장합니다.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

설정값 기준:

- `validate`: 엔티티와 DB 스키마 일치 여부만 검증합니다.
- `create-drop`: 테스트 환경에서만 사용합니다.
- `update`, `create`: Flyway와 충돌하므로 사용하지 않습니다.

## 마이그레이션 체크리스트

새 엔티티 추가 시:

- [ ] JPA 엔티티 생성
- [ ] Flyway `CREATE TABLE` 마이그레이션 파일 생성
- [ ] 필요한 인덱스 추가 마이그레이션 생성
- [ ] `ddl-auto: validate` 기준으로 스키마 일치 확인

기존 엔티티 수정 시:

- [ ] JPA 엔티티 수정
- [ ] Flyway `ALTER TABLE` 마이그레이션 파일 생성
- [ ] 로컬에서 마이그레이션 테스트

## 논리적 외래키 원칙

이 프로젝트는 JPA의 물리적 FK를 사용하지 않고 논리적 FK만 사용합니다.

금지:

- `@ManyToOne`
- `@OneToMany`
- `@OneToOne`
- `@ManyToMany`
- 엔티티 간 직접 참조

허용:

- `private Long userId`
- `private String email`
- `private Long musicId`
- `@ElementCollection`과 `@CollectionTable`을 사용한 단순 컬렉션 매핑

## 참조 관계

User 참조:

- `userId`: FocusSession, SleepSession, MeditationSession, MusicLike, ListeningHistory, SoundSpaceMusic
- `email`: Notification, TopicSubscription, Subscription

Music 참조:

- `musicId`: MusicLike, ListeningHistory, SoundSpaceMusic

## Cascade 삭제

논리적 FK를 사용하므로 cascade 삭제는 서비스 레이어에서 명시적으로 구현합니다.

User 삭제 시:

1. `userId` 기반 자식 데이터를 먼저 삭제합니다.
2. `email` 기반 자식 데이터를 삭제합니다.
3. refresh token과 User를 마지막에 삭제합니다.
4. 전체 삭제 흐름은 `@Transactional` 안에서 실행합니다.

Music 삭제 시:

1. musicId를 참조하는 MusicLike, ListeningHistory, SoundSpaceMusic을 먼저 삭제합니다.
2. Music을 마지막에 삭제합니다.
3. 전체 삭제 흐름은 `@Transactional` 안에서 실행합니다.

## 논리적 FK 엔티티 추가 체크리스트

User나 Music을 참조하는 엔티티를 추가할 때:

- [ ] 논리적 FK 필드 추가: `userId`, `email`, `musicId`
- [ ] 조회 패턴에 맞는 인덱스 설정
- [ ] 중복 방지가 필요하면 unique constraint 설정
- [ ] Delete Port 인터페이스 생성
- [ ] `deleteByUserId`, `deleteByEmail`, `deleteByMusicId` 계열 메서드 추가
- [ ] `UserWithdrawalService` 또는 `DeleteMusicService`에 cascade 삭제 로직 추가
- [ ] cascade 삭제 단위 테스트 작성

## 데이터 정합성 보장

- Service에서 참조 대상 존재 여부를 확인한 뒤 저장합니다.
- cascade 삭제는 트랜잭션 원자성을 보장해야 합니다.
- 모든 cascade 삭제 로직은 단위 테스트로 검증합니다.

더 자세한 배경과 엔티티별 참조 관계는 [document/logical-foreign-key.md](../../../document/logical-foreign-key.md)를 참고합니다.
