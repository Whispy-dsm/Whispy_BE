# Backend Testing Convention

## 필수 테스트 규칙

- Service를 생성할 때는 단위 테스트를 작성합니다.
- 비즈니스 로직을 수정했다면 관련 테스트도 수정하거나 추가합니다.
- 테스트 위치는 `src/test/java/.../domain/{도메인}/application/service/unit/`를 따릅니다.
- cascade 삭제, 도메인 검증, 예외 흐름은 테스트로 보호합니다.
- Controller를 생성하거나 수정하면 문서 인터페이스 변경과 함께 관련 controller 테스트 또는 통합 테스트를 검토합니다.

## 단위 테스트 패턴

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("서비스명 테스트")
class ServiceTest {
    @InjectMocks private TargetService service;
    @Mock private SomeDependency dependency;

    @Test
    @DisplayName("시나리오 설명")
    void whenCondition_thenExpectedResult() {
        // given

        // when

        // then
    }
}
```

## 검증 기준

- 변경 범위에 가장 가까운 테스트를 먼저 실행합니다.
- 공통 로직, 예외 처리, 설정을 건드렸다면 관련 전체 테스트 범위를 넓힙니다.
- build 후 테스트 검증을 통해 비즈니스 로직이 정상인지 확인합니다.
- 테스트를 실행하지 못한 경우에는 완료 보고에 이유와 남은 위험을 명시합니다.
