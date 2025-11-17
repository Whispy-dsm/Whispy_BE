# 논리적 외래키(Logical Foreign Key) 설정 문서

## 📌 목차
1. [개요](#1-개요)
2. [논리적 외래키란?](#2-논리적-외래키란)
3. [프로젝트 JPA 설정](#3-프로젝트-jpa-설정)
4. [논리적 외래키로 참조되는 엔티티](#4-논리적-외래키로-참조되는-엔티티)
5. [엔티티별 참조 관계](#5-엔티티별-참조-관계)
6. [CASCADE 삭제 구현](#6-cascade-삭제-구현)
7. [성능 최적화 전략](#7-성능-최적화-전략)
8. [네이밍 규칙](#8-네이밍-규칙)
9. [주의사항 및 베스트 프랙티스](#9-주의사항-및-베스트-프랙티스)

---

## 1. 개요

Whispy 프로젝트는 **헥사고날 아키텍처(Hexagonal Architecture)**를 기반으로 하며, 도메인 간 결합도를 낮추기 위해 **논리적 외래키** 방식을 전면 채택하고 있습니다.

### 핵심 설계 원칙
- ✅ JPA의 물리적 FK(`@ManyToOne`, `@OneToMany`) 사용 금지
- ✅ 논리적 FK(Long, String 타입 필드)로 엔티티 간 참조
- ✅ 애플리케이션 레벨에서 참조 무결성 관리
- ✅ CASCADE 삭제를 서비스 레이어에서 명시적으로 구현

---

## 2. 논리적 외래키란?

### 2.1 물리적 FK vs 논리적 FK

| 구분 | 물리적 FK | 논리적 FK |
|------|----------|----------|
| **구현 방식** | `@ManyToOne`, `@OneToMany` | `private Long userId` |
| **DB 제약조건** | 외래키 제약조건 생성 | 제약조건 없음 (인덱스만) |
| **참조 무결성** | DB 레벨에서 보장 | 애플리케이션 레벨에서 관리 |
| **CASCADE** | JPA가 자동 처리 | 수동으로 구현 필요 |
| **N+1 문제** | 발생 가능 | 발생하지 않음 |
| **도메인 결합도** | 높음 | 낮음 |

### 2.2 논리적 FK 선택 이유

**장점:**
- 도메인 간 결합도 감소 (Hexagonal Architecture에 적합)
- N+1 쿼리 문제 원천 차단
- 복잡한 양방향 매핑 관리 불필요
- 성능 최적화 용이 (필요한 경우에만 JOIN)
- 독립적인 도메인 테스트 가능

**단점:**
- 참조 무결성을 애플리케이션에서 직접 관리해야 함
- CASCADE 동작을 수동으로 구현해야 함

---

## 3. 프로젝트 JPA 설정

### 3.1 application.yml 설정

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create                    # 개발: create, 운영: validate
    show-sql: true                         # SQL 로깅
    properties:
      hibernate:
        format_sql: true                   # SQL 포맷팅
        jdbc:
          batch_size: 1000                 # 배치 처리 크기
          order_inserts: true              # INSERT 순서 최적화
          order_updates: true              # UPDATE 순서 최적화
    open-in-view: false                   # OSIV 비활성화 (권장)
    database: mysql
```

### 3.2 주요 설정 설명

| 설정 | 값 | 목적 |
|------|----|----|
| `batch_size` | 1000 | 대량 데이터 처리 시 배치로 묶어서 처리 |
| `order_inserts` | true | INSERT 문을 테이블별로 정렬하여 배치 효율 증가 |
| `order_updates` | true | UPDATE 문을 테이블별로 정렬하여 배치 효율 증가 |
| `open-in-view` | false | LazyLoading 문제 방지, 명시적 트랜잭션 관리 |

---

## 4. 논리적 외래키로 참조되는 엔티티

프로젝트에서 **다른 엔티티들이 논리적 FK로 참조하는 엔티티**는 2개입니다:

### 4.1 User 엔티티 (⭐⭐⭐ 가장 많이 참조됨)

**엔티티 정의:**
```java
@Entity(name = "UserJpaEntity")
@Table(name = "tbl_user")
public class UserJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // PK

    @Column(name = "email", nullable = false, unique = true)
    private String email;               // Unique Key (비즈니스 식별자)
}
```

**참조 방식:**
- `userId` (Long) - 10개 엔티티에서 참조
- `email` (String) - 3개 엔티티에서 참조

### 4.2 Music 엔티티 (⭐⭐)

**엔티티 정의:**
```java
@Entity
@Table(name = "tbl_music", indexes = {
    @Index(name = "idx_category", columnList = "category"),
    @Index(name = "idx_title", columnList = "title")
})
public class MusicJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // PK
}
```

**참조 방식:**
- `musicId` (Long) - 3개 엔티티에서 참조

---

## 5. 엔티티별 참조 관계

### 5.1 User 참조 관계

#### 5.1.1 userId (Long)로 참조하는 엔티티 - 6개

| 엔티티 | 테이블명 | 인덱스 | Unique 제약 | 용도 |
|--------|----------|--------|-------------|------|
| **FocusSessionJpaEntity** | `tbl_focus_session` | `idx_user_started(user_id, started_at)` | - | 집중 세션 |
| **SleepSessionJpaEntity** | `tbl_sleep_session` | `idx_user_started(user_id, started_at)` | - | 수면 세션 |
| **MeditationSessionJpaEntity** | `tbl_meditation_session` | `idx_user_started(user_id, started_at)` | - | 명상 세션 |
| **MusicLikeJpaEntity** | `tbl_music_like` | - | `(user_id, music_id)` | 음악 좋아요 |
| **ListeningHistoryJpaEntity** | `tbl_listening_history` | - | `(user_id, music_id)` | 청취 기록 |
| **SoundSpaceMusicJpaEntity** | `tbl_soundspace_music` | - | `(user_id, music_id)` | 사운드스페이스 |

**예시 코드:**
```java
@Entity(name = "FocusSessionJpaEntity")
@Table(name = "tbl_focus_session", indexes = {
    @Index(name = "idx_user_started", columnList = "user_id, started_at")
})
public class FocusSessionJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;                           // 논리적 FK → User

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;
}
```

#### 5.1.2 email (String)로 참조하는 엔티티 - 3개

| 엔티티 | 테이블명 | 인덱스 | Unique 제약 | 용도 |
|--------|----------|--------|-------------|------|
| **NotificationJpaEntity** | `tbl_notification` | - | - | 알림 |
| **TopicSubscriptionJpaEntity** | `tbl_topic_subscription` | `idx_topic_sub_email(email)` | `(email, topic)` | 토픽 구독 |
| **SubscriptionJpaEntity** | `tbl_subscription` | `idx_subscription_email_state(email, subscription_state)` | - | 결제 정보 |

**예시 코드:**
```java
@Entity(name = "NotificationJpaEntity")
@Table(name = "tbl_notification")
public class NotificationJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;                          // 논리적 FK → User
}
```

**email 사용 이유:**
- FCM 푸시 알림은 email 기반으로 전송
- 외부 서비스 연동 시 email이 주요 식별자
- 결제 시스템 연동 시 email 필요

### 5.2 Music 참조 관계

#### 5.2.1 musicId (Long)로 참조하는 엔티티 - 3개

| 엔티티 | 테이블명 | Unique 제약 | 용도 |
|--------|----------|-------------|------|
| **MusicLikeJpaEntity** | `tbl_music_like` | `(user_id, music_id)` | 음악 좋아요 |
| **ListeningHistoryJpaEntity** | `tbl_listening_history` | `(user_id, music_id)` | 청취 기록 |
| **SoundSpaceMusicJpaEntity** | `tbl_soundspace_music` | `(user_id, music_id)` | 사운드스페이스 |

**예시 코드:**
```java
@Entity(name = "MusicLikeJpaEntity")
@Table(name = "tbl_music_like",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "music_id"})
    })
public class MusicLikeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;                           // 논리적 FK → User

    @Column(name = "music_id", nullable = false)
    private Long musicId;                          // 논리적 FK → Music
}
```

### 5.3 참조 관계 다이어그램

```
User (id, email)
 ├─ userId 기반 참조 (6개)
 │   ├─ FocusSession
 │   ├─ SleepSession
 │   ├─ MeditationSession
 │   ├─ MusicLike
 │   ├─ ListeningHistory
 │   └─ SoundSpaceMusic
 │
 └─ email 기반 참조 (3개)
     ├─ Notification
     ├─ TopicSubscription
     └─ Subscription

Music (id)
 ├─ musicId 기반 참조 (3개)
     ├─ MusicLike
     ├─ ListeningHistory
     └─ SoundSpaceMusic
```

---

## 6. CASCADE 삭제 구현

논리적 FK를 사용하므로, CASCADE 삭제는 **애플리케이션 레벨**에서 명시적으로 구현해야 합니다.

### 6.1 User 삭제 시 CASCADE

**구현 위치:** `UserWithdrawalService.java:42`

```java
@Service
@RequiredArgsConstructor
@Transactional  // 모든 삭제가 하나의 트랜잭션으로 처리
public class UserWithdrawalService implements UserWithdrawalUseCase {

    // userId 기반 Delete Ports
    private final DeleteFocusSessionPort deleteFocusSessionPort;
    private final DeleteSleepSessionPort deleteSleepSessionPort;
    private final DeleteMeditationSessionPort deleteMeditationSessionPort;
    private final DeleteMusicLikePort deleteMusicLikePort;
    private final DeleteListeningHistoryPort deleteListeningHistoryPort;
    private final DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;

    // email 기반 Delete Ports
    private final DeleteNotificationPort deleteNotificationPort;
    private final DeleteTopicSubscriptionPort deleteTopicSubscriptionPort;

    @Override
    @Transactional
    public void withdrawal() {
        User currentUser = userFacadeUseCase.currentUser();
        Long userId = currentUser.id();
        String email = currentUser.email();

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        // 1️⃣ userId 기반 CASCADE 삭제 (6개)
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        deleteFocusSessionPort.deleteByUserId(userId);
        deleteSleepSessionPort.deleteByUserId(userId);
        deleteMeditationSessionPort.deleteByUserId(userId);
        deleteMusicLikePort.deleteByUserId(userId);
        deleteListeningHistoryPort.deleteByUserId(userId);
        deleteSoundSpaceMusicPort.deleteByUserId(userId);

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        // 2️⃣ email 기반 CASCADE 삭제 (2개)
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        deleteNotificationPort.deleteByEmail(email);
        deleteTopicSubscriptionPort.deleteByEmail(email);

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        // 3️⃣ 보존되는 데이터
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        // Subscription은 결제 기록 보존을 위해 삭제하지 않음

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        // 4️⃣ User 삭제
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        refreshTokenRepository.deleteById(email);
        userDeletePort.deleteById(userId);
    }
}
```

**삭제되는 엔티티 (8개):**
1. FocusSession
2. SleepSession
3. MeditationSession
4. MusicLike
5. ListeningHistory
6. SoundSpaceMusic
7. Notification
8. TopicSubscription

**보존되는 엔티티 (1개):**
- **Subscription** - 결제 기록 법적 보존 의무

### 6.2 Music 삭제 시 CASCADE

**구현 위치:** `DeleteMusicService.java:28`

```java
@Service
@RequiredArgsConstructor
public class DeleteMusicService implements DeleteMusicUseCase {

    private final QueryMusicPort queryMusicPort;
    private final MusicDeletePort musicDeletePort;
    private final DeleteMusicLikePort deleteMusicLikePort;
    private final DeleteListeningHistoryPort deleteListeningHistoryPort;
    private final DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;

    @Transactional
    @Override
    public void execute(Long id) {
        if (!queryMusicPort.existsById(id)) {
            throw MusicNotFoundException.EXCEPTION;
        }

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        // CASCADE 삭제 (논리적 외래키 처리)
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        deleteMusicLikePort.deleteAllByMusicId(id);
        deleteListeningHistoryPort.deleteAllByMusicId(id);
        deleteSoundSpaceMusicPort.deleteAllByMusicId(id);

        // Music 삭제
        musicDeletePort.deleteById(id);
    }
}
```

**삭제되는 엔티티 (3개):**
1. MusicLike
2. ListeningHistory
3. SoundSpaceMusic

### 6.3 CASCADE 삭제 요약 테이블

| 삭제 대상 | CASCADE 여부 | 삭제되는 엔티티 수 | 구현 위치 |
|-----------|-------------|-------------------|-----------|
| **User** | ✅ | 8개 (총 9개 중) | `UserWithdrawalService:42` |
| **Music** | ✅ | 3개 (전체) | `DeleteMusicService:28` |
| **Announcement** | ❌ | 0개 | `DeleteAnnouncementService:17` |
| **Admin** | ❌ | 0개 | - |

---

## 7. 성능 최적화 전략

### 7.1 인덱스 전략

논리적 FK 컬럼에는 **반드시 인덱스를 생성**하여 조회 성능을 보장합니다.

| 엔티티 | 인덱스 정의 | 목적 |
|--------|------------|------|
| **FocusSession** | `@Index(name = "idx_user_started", columnList = "user_id, started_at")` | 사용자별 세션 목록 조회 최적화 |
| **SleepSession** | `@Index(name = "idx_user_started", columnList = "user_id, started_at")` | 사용자별 세션 목록 조회 최적화 |
| **MeditationSession** | `@Index(name = "idx_user_started", columnList = "user_id, started_at")` | 사용자별 세션 목록 조회 최적화 |
| **Subscription** | `@Index(name = "idx_subscription_email_state", columnList = "email, subscription_state")` | 활성 구독 조회 최적화 |
| **TopicSubscription** | `@Index(name = "idx_topic_sub_topic_subscribed", columnList = "topic, subscribed")` | 토픽별 구독자 조회 최적화 |

**인덱스 설정 예시:**
```java
@Entity(name = "FocusSessionJpaEntity")
@Table(name = "tbl_focus_session", indexes = {
    @Index(name = "idx_user_started", columnList = "user_id, started_at")
})
public class FocusSessionJpaEntity extends BaseTimeEntity {
    // ...
}
```

### 7.2 Unique Constraint로 무결성 보장

복합 컬럼에 `@UniqueConstraint`를 설정하여 중복 데이터를 방지합니다.

| 엔티티 | Unique Constraint | 목적 |
|--------|------------------|------|
| **MusicLike** | `(user_id, music_id)` | 중복 좋아요 방지 |
| **ListeningHistory** | `(user_id, music_id)` | 중복 청취 기록 방지 |
| **SoundSpaceMusic** | `(user_id, music_id)` | 중복 추가 방지 |
| **TopicSubscription** | `(email, topic)` | 중복 구독 방지 |

**Unique 제약 설정 예시:**
```java
@Entity(name = "MusicLikeJpaEntity")
@Table(name = "tbl_music_like",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "music_id"})
    })
public class MusicLikeJpaEntity {
    // ...
}
```

### 7.3 배치 처리 최적화

대량 데이터 삭제/삽입 시 배치 처리로 성능을 향상시킵니다.

```yaml
# application.yml
hibernate:
  jdbc:
    batch_size: 1000           # 1000개씩 묶어서 처리
    order_inserts: true        # INSERT 순서 최적화
    order_updates: true        # UPDATE 순서 최적화
```

**배치 삭제 예시:**
```java
// NotificationPersistenceAdapter.java
@Override
public void deleteAllByIdInBatch(List<Long> ids) {
    QNotificationJpaEntity notification = QNotificationJpaEntity.notificationJpaEntity;

    jpaQueryFactory
        .delete(notification)
        .where(notification.id.in(ids))
        .execute();  // 한 번에 배치로 삭제
}
```

---

## 8. 네이밍 규칙

### 8.1 삭제 메서드 네이밍

| 패턴 | 용도 | 예시 | 사용 시점 |
|------|------|------|-----------|
| `deleteByUserId` | 특정 유저의 모든 데이터 삭제 | `deleteByUserId(Long userId)` | User 탈퇴 시 |
| `deleteByEmail` | 특정 이메일의 모든 데이터 삭제 | `deleteByEmail(String email)` | User 탈퇴 시 |
| `deleteAllByMusicId` | 특정 음악의 모든 참조 삭제 | `deleteAllByMusicId(Long musicId)` | Music 삭제 시 |
| `deleteById` | 단일 레코드 삭제 | `deleteById(Long id)` | 일반 삭제 |
| `deleteAllByIdInBatch` | 여러 ID를 배치로 삭제 | `deleteAllByIdInBatch(List<Long> ids)` | 대량 삭제 |

### 8.2 Port 인터페이스 네이밍

**Delete Port 예시:**
```java
public interface DeleteFocusSessionPort {
    void deleteById(Long id);              // 단일 삭제
    void deleteByUserId(Long userId);      // CASCADE 삭제용
}
```

**Composite Port 예시:**
```java
public interface FocusSessionPort extends
    FocusSessionSavePort,
    QueryFocusSessionPort,
    DeleteFocusSessionPort {
    // 여러 Port를 상속하여 하나의 Port로 관리
}
```

### 8.3 Repository 메서드 네이밍

Spring Data JPA 네이밍 규칙을 따릅니다:

```java
public interface FocusSessionRepository extends JpaRepository<FocusSessionJpaEntity, Long> {

    // 조회
    Page<FocusSessionJpaEntity> findByUserIdOrderByStartedAtDesc(Long userId, Pageable pageable);
    Optional<FocusSessionJpaEntity> findByIdAndUserId(Long id, Long userId);

    // 삭제
    void deleteByUserId(Long userId);
}
```

---

## 9. 주의사항 및 베스트 프랙티스

### 9.1 DO - 반드시 지켜야 할 사항

#### ✅ 1. 트랜잭션 필수
CASCADE 삭제는 반드시 `@Transactional` 안에서 실행해야 합니다.

```java
@Transactional  // 필수!
public void withdrawal() {
    deleteFocusSessionPort.deleteByUserId(userId);
    deleteSleepSessionPort.deleteByUserId(userId);
    // ... 모든 삭제가 하나의 트랜잭션으로 처리됨
    userDeletePort.deleteById(userId);
}
```

#### ✅ 2. 삭제 순서 준수
**자식 데이터를 먼저 삭제**하고, **부모 데이터를 나중에 삭제**해야 합니다.

```java
// ✅ 올바른 순서
deleteMusicLikePort.deleteAllByMusicId(id);         // 1. 자식 먼저
deleteListeningHistoryPort.deleteAllByMusicId(id);  // 2. 자식 먼저
deleteSoundSpaceMusicPort.deleteAllByMusicId(id);   // 3. 자식 먼저
musicDeletePort.deleteById(id);                      // 4. 부모 마지막

// ❌ 잘못된 순서
musicDeletePort.deleteById(id);                      // 부모를 먼저 삭제하면 안 됨!
deleteMusicLikePort.deleteAllByMusicId(id);
```

#### ✅ 3. 인덱스 필수 설정
논리적 FK 컬럼에는 반드시 인덱스를 추가해야 합니다.

```java
@Entity
@Table(name = "tbl_focus_session", indexes = {
    @Index(name = "idx_user_started", columnList = "user_id, started_at")
})
public class FocusSessionJpaEntity {
    @Column(name = "user_id", nullable = false)
    private Long userId;  // 인덱스 필수!
}
```

#### ✅ 4. Unique 제약 설정
비즈니스 규칙에 맞는 복합 Unique Constraint를 설정해야 합니다.

```java
@Entity
@Table(name = "tbl_music_like",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "music_id"})
    })
public class MusicLikeJpaEntity {
    // 사용자는 같은 음악에 중복으로 좋아요를 누를 수 없음
}
```

### 9.2 DON'T - 금지 사항

#### ❌ 1. 물리적 FK 혼용 금지
`@ManyToOne`, `@OneToMany`, `@JoinColumn` 등을 사용하면 안 됩니다.

```java
// ❌ 잘못된 예시
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private UserJpaEntity user;

// ✅ 올바른 예시
@Column(name = "user_id", nullable = false)
private Long userId;
```

#### ❌ 2. Lazy Loading 의존 금지
논리적 FK는 자동으로 JOIN되지 않습니다. 필요한 경우 명시적으로 JOIN해야 합니다.

```java
// ❌ 잘못된 예시 - user 객체는 존재하지 않음
focusSession.getUser().getName();

// ✅ 올바른 예시 - 명시적으로 조회
User user = userQueryPort.findById(focusSession.getUserId());
user.getName();
```

#### ❌ 3. CASCADE 누락 주의
새로운 엔티티가 추가될 때, CASCADE 삭제 로직을 누락하지 않도록 주의해야 합니다.

```java
// 새로운 엔티티 추가 시 체크리스트:
// 1. Delete Port 생성 (예: DeleteNewEntityPort)
// 2. deleteByUserId 또는 deleteByEmail 메서드 추가
// 3. UserWithdrawalService에 CASCADE 삭제 로직 추가
```

### 9.3 보존해야 할 데이터

일부 엔티티는 **법적/비즈니스적 이유**로 사용자 삭제 시에도 보존해야 합니다.

| 엔티티 | 보존 이유 | 참조 필드 |
|--------|----------|----------|
| **Subscription** | 결제 기록 법적 보존 의무 (전자상거래법) | `email` |

**참고:**
- WithdrawalReason은 User를 참조하지 않으며, 독립적인 통계 데이터로 관리됩니다.

### 9.5 새로운 엔티티 추가 시 체크리스트

새로운 엔티티가 User나 Music을 참조하는 경우, 다음 절차를 따라야 합니다:

1. **엔티티 정의**
   - [ ] `userId` 또는 `email` 또는 `musicId` 필드 추가
   - [ ] 인덱스 설정 (`@Index`)
   - [ ] 필요시 Unique 제약 설정 (`@UniqueConstraint`)

2. **Port 정의**
   - [ ] Delete Port 인터페이스 생성
   - [ ] `deleteByUserId` 또는 `deleteByEmail` 메서드 정의
   - [ ] Composite Port에 Delete Port 상속 추가

3. **Repository 정의**
   - [ ] `deleteByUserId` 또는 `deleteByEmail` 메서드 추가

4. **Adapter 구현**
   - [ ] PersistenceAdapter에서 Delete Port 메서드 구현
   - [ ] Repository 메서드 호출

5. **CASCADE 삭제 구현**
   - [ ] `UserWithdrawalService`에 삭제 로직 추가 (User 참조 시)
   - [ ] `DeleteMusicService`에 삭제 로직 추가 (Music 참조 시)

6. **테스트**
   - [ ] User 삭제 시 함께 삭제되는지 확인
   - [ ] 트랜잭션 롤백 시 모두 롤백되는지 확인

---

## 부록: 전체 참조 관계도

```
┌─────────────────────────────────────────────────────────────┐
│                        User (id, email)                      │
└─────────────────────────────────────────────────────────────┘
          │
          ├─ userId 참조 (6개)
          │   ├─ FocusSession          → deleteByUserId()
          │   ├─ SleepSession          → deleteByUserId()
          │   ├─ MeditationSession     → deleteByUserId()
          │   ├─ MusicLike             → deleteByUserId()
          │   ├─ ListeningHistory      → deleteByUserId()
          │   └─ SoundSpaceMusic       → deleteByUserId()
          │
          └─ email 참조 (3개)
              ├─ Notification          → deleteByEmail()
              ├─ TopicSubscription     → deleteByEmail()
              └─ Subscription          → 🔒 보존 (결제 기록)


┌─────────────────────────────────────────────────────────────┐
│                        Music (id)                            │
└─────────────────────────────────────────────────────────────┘
          │
          └─ musicId 참조 (3개)
              ├─ MusicLike             → deleteAllByMusicId()
              ├─ ListeningHistory      → deleteAllByMusicId()
              └─ SoundSpaceMusic       → deleteAllByMusicId()
```

---

## 문서 히스토리

| 버전 | 날짜 | 작성자 | 변경 내용 |
|------|------|--------|----------|
| 1.0.0 | 2025-01-XX | - | 초기 작성 |

---

## 참고 자료

- [Spring Data JPA Reference Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate User Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html)
- [Hexagonal Architecture 가이드](https://alistair.cockburn.us/hexagonal-architecture/)
