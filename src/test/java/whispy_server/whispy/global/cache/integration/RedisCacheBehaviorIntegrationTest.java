package whispy_server.whispy.global.cache.integration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicSavePort;
import whispy_server.whispy.domain.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.domain.music.application.service.CreateMusicService;
import whispy_server.whispy.domain.music.application.service.SearchMusicCategoryCacheService;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.FocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.in.GetFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.focus.summary.application.service.GetFocusStatisticsService;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response.SleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepDetailedAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.in.GetSleepStatisticsUseCase;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.out.QuerySleepStatisticsPort;
import whispy_server.whispy.domain.statistics.sleep.summary.application.service.GetSleepStatisticsService;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangeProfileRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyProfileResponse;
import whispy_server.whispy.domain.user.application.port.in.ChangeProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.GetMyProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.application.service.ChangeProfileService;
import whispy_server.whispy.domain.user.application.service.GetMyProfileService;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.cache.key.StatisticsSummaryKeyGenerator;
import whispy_server.whispy.global.cache.key.UserProfileKeyGenerator;
import whispy_server.whispy.global.cache.version.StatisticsCacheDomain;
import whispy_server.whispy.global.cache.version.StatisticsCacheVersionManager;
import whispy_server.whispy.global.config.redis.RedisConfig;
import whispy_server.whispy.global.security.auth.AuthDetails;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(RedisCacheBehaviorIntegrationTest.TestConfig.class)
@DisplayName("Redis cache behavior integration test")
class RedisCacheBehaviorIntegrationTest {

    private static final Long TEST_USER_ID = 1L;

    @Configuration
    @EnableCaching
    static class TestConfig {

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(
                    RedisConfig.MUSIC_CATEGORY_SEARCH_CACHE,
                    RedisConfig.USER_MY_PROFILE_CACHE,
                    RedisConfig.STATS_FOCUS_SUMMARY_CACHE,
                    RedisConfig.STATS_SLEEP_SUMMARY_CACHE
            );
        }

        @Bean
        PlatformTransactionManager transactionManager() {
            return new ResourcelessTransactionManager();
        }

        @Bean
        TestSearchMusicPort testSearchMusicPort() {
            return new TestSearchMusicPort();
        }

        @Bean
        SearchMusicPort searchMusicPort(TestSearchMusicPort port) {
            return port;
        }

        @Bean
        MusicSavePort musicSavePort() {
            return music -> music;
        }

        @Bean
        SearchMusicCategoryCacheService searchMusicCategoryCacheService(SearchMusicPort searchMusicPort) {
            return new SearchMusicCategoryCacheService(searchMusicPort);
        }

        @Bean
        CreateMusicService createMusicService(MusicSavePort musicSavePort) {
            return new CreateMusicService(musicSavePort);
        }

        @Bean
        TestUserState testUserState() {
            return new TestUserState();
        }

        @Bean
        UserFacadeUseCase userFacadeUseCase(TestUserState state) {
            return new UserFacadeUseCase() {
                @Override
                public User currentUser() {
                    state.currentUserCalls.incrementAndGet();
                    return state.currentUser.get();
                }

                @Override
                public User getUserById(Long userId) {
                    return state.currentUser.get();
                }
            };
        }

        @Bean
        UserSavePort userSavePort(TestUserState state) {
            return user -> {
                state.saveCalls.incrementAndGet();
                state.currentUser.set(user);
                return user;
            };
        }

        @Bean
        GetMyProfileService getMyProfileService(UserFacadeUseCase userFacadeUseCase) {
            return new GetMyProfileService(userFacadeUseCase);
        }

        @Bean
        ChangeProfileService changeProfileService(UserFacadeUseCase userFacadeUseCase, UserSavePort userSavePort) {
            return new ChangeProfileService(userFacadeUseCase, userSavePort);
        }

        @Bean
        InMemoryStatisticsCacheVersionManager statisticsCacheVersionManager() {
            return new InMemoryStatisticsCacheVersionManager();
        }

        @Bean
        StatisticsSummaryKeyGenerator statisticsSummaryKeyGenerator(StatisticsCacheVersionManager manager) {
            return new StatisticsSummaryKeyGenerator(manager);
        }

        @Bean
        UserProfileKeyGenerator userProfileKeyGenerator() {
            return new UserProfileKeyGenerator();
        }

        @Bean
        TestQueryFocusStatisticsPort testQueryFocusStatisticsPort() {
            return new TestQueryFocusStatisticsPort();
        }

        @Bean
        QueryFocusStatisticsPort queryFocusStatisticsPort(TestQueryFocusStatisticsPort port) {
            return port;
        }

        @Bean
        GetFocusStatisticsService getFocusStatisticsService(
                QueryFocusStatisticsPort queryFocusStatisticsPort,
                UserFacadeUseCase userFacadeUseCase
        ) {
            return new GetFocusStatisticsService(queryFocusStatisticsPort, userFacadeUseCase);
        }

        @Bean
        TestQuerySleepStatisticsPort testQuerySleepStatisticsPort() {
            return new TestQuerySleepStatisticsPort();
        }

        @Bean
        QuerySleepStatisticsPort querySleepStatisticsPort(TestQuerySleepStatisticsPort port) {
            return port;
        }

        @Bean
        GetSleepStatisticsService getSleepStatisticsService(
                QuerySleepStatisticsPort querySleepStatisticsPort,
                UserFacadeUseCase userFacadeUseCase
        ) {
            return new GetSleepStatisticsService(querySleepStatisticsPort, userFacadeUseCase);
        }
    }

    static class TestUserState {
        private final AtomicReference<User> currentUser = new AtomicReference<>();
        private final AtomicInteger currentUserCalls = new AtomicInteger();
        private final AtomicInteger saveCalls = new AtomicInteger();
    }

    static class TestSearchMusicPort implements SearchMusicPort {
        private final AtomicInteger searchByCategoryCalls = new AtomicInteger();

        @Override
        public org.springframework.data.domain.Page<Music> searchByTitle(String title, org.springframework.data.domain.Pageable pageable) {
            throw new UnsupportedOperationException("Not needed in this test");
        }

        @Override
        public org.springframework.data.domain.Page<Music> searchByCategory(MusicCategory category, org.springframework.data.domain.Pageable pageable) {
            searchByCategoryCalls.incrementAndGet();
            Music music = new Music(
                    1L,
                    category.name() + "-title",
                    "artist",
                    "description",
                    "/music/test.mp3",
                    180,
                    category,
                    "https://example.com/banner.jpg",
                    "https://example.com/video.mp4"
            );
            return new org.springframework.data.domain.PageImpl<>(List.of(music), pageable, 1);
        }
    }

    static class InMemoryStatisticsCacheVersionManager extends StatisticsCacheVersionManager {
        private final Map<String, AtomicLong> versions = new ConcurrentHashMap<>();

        InMemoryStatisticsCacheVersionManager() {
            super(null);
        }

        @Override
        public long getUserVersion(Long userId, StatisticsCacheDomain domain) {
            return versions.getOrDefault(key(userId, domain), new AtomicLong(0)).get();
        }

        @Override
        public void bumpUserVersion(Long userId, StatisticsCacheDomain domain) {
            versions.computeIfAbsent(key(userId, domain), ignored -> new AtomicLong(0)).incrementAndGet();
        }

        @Override
        public void bumpUserVersionAfterCommit(Long userId, StatisticsCacheDomain domain) {
            bumpUserVersion(userId, domain);
        }

        private String key(Long userId, StatisticsCacheDomain domain) {
            return userId + ":" + domain.name();
        }
    }

    static class TestQueryFocusStatisticsPort implements QueryFocusStatisticsPort {
        private final AtomicInteger aggregateCalls = new AtomicInteger();

        @Override
        public List<whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
            return Collections.emptyList();
        }

        @Override
        public FocusAggregationDto aggregateByPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
            aggregateCalls.incrementAndGet();
            return new FocusAggregationDto(2, 120);
        }

        @Override
        public int sumMinutesByDate(Long userId, LocalDate date) {
            return 30;
        }

        @Override
        public List<TagMinutesDto> aggregateByTag(Long userId, LocalDateTime start, LocalDateTime end) {
            return List.of(new TagMinutesDto(FocusTag.WORK, 120));
        }

        @Override
        public int countDistinctDays(Long userId, LocalDateTime start, LocalDateTime end) {
            return 1;
        }
    }

    static class TestQuerySleepStatisticsPort implements QuerySleepStatisticsPort {
        private final AtomicInteger aggregateCalls = new AtomicInteger();

        @Override
        public List<SleepSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
            return List.of(new SleepSessionDto(
                    1L,
                    userId,
                    LocalDateTime.of(2024, 1, 10, 23, 0),
                    LocalDateTime.of(2024, 1, 11, 7, 0),
                    8 * 60 * 60,
                    LocalDateTime.of(2024, 1, 11, 7, 0)
            ));
        }

        @Override
        public SleepAggregationDto aggregateByPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
            return new SleepAggregationDto(1, 480);
        }

        @Override
        public int sumMinutesByDate(Long userId, LocalDate date) {
            return 480;
        }

        @Override
        public SleepDetailedAggregationDto aggregateDetailedStatistics(Long userId, LocalDateTime start, LocalDateTime end) {
            aggregateCalls.incrementAndGet();
            return new SleepDetailedAggregationDto(1, 480, 480, 23 * 60, 7 * 60);
        }
    }

    @org.springframework.beans.factory.annotation.Autowired
    private CacheManager cacheManager;

    @org.springframework.beans.factory.annotation.Autowired
    private SearchMusicCategoryCacheService searchMusicCategoryCacheService;

    @org.springframework.beans.factory.annotation.Autowired
    private CreateMusicUseCase createMusicService;

    @org.springframework.beans.factory.annotation.Autowired
    private TestSearchMusicPort testSearchMusicPort;

    @org.springframework.beans.factory.annotation.Autowired
    private GetMyProfileUseCase getMyProfileService;

    @org.springframework.beans.factory.annotation.Autowired
    private ChangeProfileUseCase changeProfileService;

    @org.springframework.beans.factory.annotation.Autowired
    private TestUserState testUserState;

    @org.springframework.beans.factory.annotation.Autowired
    private GetFocusStatisticsUseCase getFocusStatisticsService;

    @org.springframework.beans.factory.annotation.Autowired
    private GetSleepStatisticsUseCase getSleepStatisticsService;

    @org.springframework.beans.factory.annotation.Autowired
    private TestQueryFocusStatisticsPort testQueryFocusStatisticsPort;

    @org.springframework.beans.factory.annotation.Autowired
    private TestQuerySleepStatisticsPort testQuerySleepStatisticsPort;

    @org.springframework.beans.factory.annotation.Autowired
    private InMemoryStatisticsCacheVersionManager statisticsCacheVersionManager;

    @BeforeEach
    void setUp() {
        AuthDetails authDetails = new AuthDetails(TEST_USER_ID, Role.USER.name(), Collections.emptyMap());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authDetails, null, authDetails.getAuthorities())
        );

        testUserState.currentUser.set(createUser("TestUser", "https://example.com/profile.jpg", Gender.MALE));
        testUserState.currentUserCalls.set(0);
        testUserState.saveCalls.set(0);
        testSearchMusicPort.searchByCategoryCalls.set(0);
        testQueryFocusStatisticsPort.aggregateCalls.set(0);
        testQuerySleepStatisticsPort.aggregateCalls.set(0);
        cacheManager.getCache(RedisConfig.MUSIC_CATEGORY_SEARCH_CACHE).clear();
        cacheManager.getCache(RedisConfig.USER_MY_PROFILE_CACHE).clear();
        cacheManager.getCache(RedisConfig.STATS_FOCUS_SUMMARY_CACHE).clear();
        cacheManager.getCache(RedisConfig.STATS_SLEEP_SUMMARY_CACHE).clear();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("music category cache는 첫 페이지를 캐시하고 생성 시 무효화된다")
    void musicCategoryCacheCachesFirstPageAndEvictsOnCreate() {
        org.springframework.data.domain.Pageable firstPage = org.springframework.data.domain.PageRequest.of(0, 10);

        searchMusicCategoryCacheService.searchByMusicCategory(MusicCategory.NATURE, firstPage);
        searchMusicCategoryCacheService.searchByMusicCategory(MusicCategory.NATURE, firstPage);

        assertThat(testSearchMusicPort.searchByCategoryCalls.get()).isEqualTo(1);

        createMusicService.execute(new CreateMusicRequest(
                "new-title",
                "artist",
                "description",
                "/music/new.mp3",
                200,
                MusicCategory.NATURE,
                "https://example.com/banner.jpg",
                "https://example.com/video.mp4"
        ));

        searchMusicCategoryCacheService.searchByMusicCategory(MusicCategory.NATURE, firstPage);

        assertThat(testSearchMusicPort.searchByCategoryCalls.get()).isEqualTo(2);
    }

    @Test
    @DisplayName("user profile cache는 조회를 캐시하고 프로필 변경 시 캐시를 덮어쓴다")
    void userProfileCacheCachesReadAndUpdatesOnChange() {
        MyProfileResponse first = getMyProfileService.execute();
        MyProfileResponse second = getMyProfileService.execute();

        assertThat(first.name()).isEqualTo("TestUser");
        assertThat(second.name()).isEqualTo("TestUser");
        assertThat(testUserState.currentUserCalls.get()).isEqualTo(1);

        MyProfileResponse updated = changeProfileService.execute(new ChangeProfileRequest(
                "UpdatedName",
                "https://example.com/updated.jpg",
                Gender.FEMALE
        ));

        MyProfileResponse cachedAfterUpdate = getMyProfileService.execute();

        assertThat(updated.name()).isEqualTo("UpdatedName");
        assertThat(cachedAfterUpdate.name()).isEqualTo("UpdatedName");
        assertThat(cachedAfterUpdate.profileImageUrl()).isEqualTo("https://example.com/updated.jpg");
        assertThat(testUserState.saveCalls.get()).isEqualTo(1);
        assertThat(testUserState.currentUserCalls.get()).isEqualTo(2);
    }

    @Test
    @DisplayName("statistics summary cache는 도메인별 버전이 분리되어 focus 무효화가 sleep 캐시에 영향을 주지 않는다")
    void statisticsSummaryCacheSeparatesVersionsByDomain() {
        LocalDate date = LocalDate.of(2024, 1, 15);

        FocusStatisticsResponse firstFocus = getFocusStatisticsService.execute(FocusPeriodType.WEEK, date);
        FocusStatisticsResponse secondFocus = getFocusStatisticsService.execute(FocusPeriodType.WEEK, date);
        SleepStatisticsResponse firstSleep = getSleepStatisticsService.execute(SleepPeriodType.WEEK, date);
        SleepStatisticsResponse secondSleep = getSleepStatisticsService.execute(SleepPeriodType.WEEK, date);

        assertThat(firstFocus.totalMinutes()).isEqualTo(120);
        assertThat(secondFocus.totalMinutes()).isEqualTo(120);
        assertThat(firstSleep.averageMinutes()).isEqualTo(480);
        assertThat(secondSleep.averageMinutes()).isEqualTo(480);
        assertThat(testQueryFocusStatisticsPort.aggregateCalls.get()).isEqualTo(1);
        assertThat(testQuerySleepStatisticsPort.aggregateCalls.get()).isEqualTo(1);

        statisticsCacheVersionManager.bumpUserVersion(TEST_USER_ID, StatisticsCacheDomain.FOCUS);

        getFocusStatisticsService.execute(FocusPeriodType.WEEK, date);
        getSleepStatisticsService.execute(SleepPeriodType.WEEK, date);

        assertThat(testQueryFocusStatisticsPort.aggregateCalls.get()).isEqualTo(2);
        assertThat(testQuerySleepStatisticsPort.aggregateCalls.get()).isEqualTo(1);
    }

    private User createUser(String name, String profileImageUrl, Gender gender) {
        return new User(
                TEST_USER_ID,
                "test@example.com",
                "password",
                name,
                profileImageUrl,
                gender,
                Role.USER,
                "일반 로그인",
                null,
                LocalDateTime.now().minusDays(10)
        );
    }
}
