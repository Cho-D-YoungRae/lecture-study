package me.study.inflearnjavatest.junit;

import me.study.inflearnjavatest.domain.Study;
import me.study.inflearnjavatest.study.StudyStatus;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(FindSlowTestExtension.class)
class StudyTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("StudyTest.beforeAll>>>>>>>>>>>>>>>>>>>>");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("StudyTest.afterAll<<<<<<<<<<<<<<<<<<<<");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("StudyTest.beforeEach>>>>>>>>>>");
    }

    @AfterEach
    void afterEach() {
        System.out.println("StudyTest.afterEach<<<<<<<<<<");
    }


    @DisplayName("스터디 만들기")
    @FastTest // @Tag("fast") @Test
    void create() {
        System.out.println("StudyTest.create");

        Study study = new Study(10);

        assertAll(
                () -> assertNotNull(study),
                () -> org.junit.jupiter.api.Assertions.assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        "스터디를 처음 만들면 상태값이 DRAFT여야 한다."),
                // 에러 메세지가 복잡한 경우(문자열 연산 비용이 걱정될 경우) lambda 로 에러메세지가 제공되면
                // 문자열 연산을 필요한 시점에만 할 수 있게 한다. -> Test 가 실패했을 때만
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        () -> "스터디를 처음 만들면 상태값이 " + StudyStatus.DRAFT + "여야 한다."),
                () -> assertTrue(study.getLimitCount() > 0,
                        "스터디 최대 가능 인원은 0명보다 커야한다.")
        );

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new Study(0));
        assertEquals("limit은 0보다 커야한다.", exception.getMessage());

        assertTimeout(Duration.ofSeconds(10), () -> Study.builder().limitCount(10).build());
    }

    @Test
    @SlowTest // @Tag("slow") @Test
    void create_assertj() {
        System.out.println("StudyTest.create_assertj");

        Study study = new Study(10);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(study).isNotNull();
            softAssertions.assertThat(study.getStatus())
                    .withFailMessage("스터디를 처음 만들면 상태값이 DRAFT여야 한다.")
                    .isEqualTo(StudyStatus.DRAFT);
            softAssertions.assertThat(study.getStatus())
                    .withFailMessage(()-> "스터디를 처음 만들면 상태값이 " + StudyStatus.DRAFT + "여야 한다.")
                    .isEqualTo(StudyStatus.DRAFT);
            softAssertions.assertThat(study.getLimitCount() > 0)
                    .withFailMessage("스터디 최대 가능 인원은 0명보다 커야한다.")
                    .isTrue();
        });

        Assertions.assertThatThrownBy(() -> new Study(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("limit은 0보다 커야한다.");

        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> new Study(0))
                .withMessageContaining("limit은 0보다 커야한다.");

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Study(0))
                .withMessageContaining("limit은 0보다 커야한다.");
    }

    @Test
    @Disabled("Disabled 공부중")
    void disabled() {
        System.out.println("StudyTest.disabled");
    }

    @RepeatedTest(10)
    void basicRepeatedTest(RepetitionInfo repetitionInfo) {
        System.out.println("StudyTest.repeatedTest("
                + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions() + ")");
    }

    @DisplayName("반복 테스트")
    @RepeatedTest(value = 10, name = "{displayName} ({currentRepetition}/{totalRepetitions})")
    void repeatedTest(RepetitionInfo repetitionInfo) {
        System.out.println("StudyTest.repeatedTest("
                + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions() + ")");
    }

    @DisplayName("기본 ParameterizedTest")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
    void basicParameterizedTest(String message) {
        System.out.println(message);
    }

    @DisplayName("ParameterizedTest")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
    @NullAndEmptySource // @NullSource @EmptySource
    void parameterizedTest(String message) {
        System.out.println(message);
    }

    @DisplayName("ParameterizedTest with int values")
    @ParameterizedTest(name = "{index} {displayName} limit={0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTestWithInt(int limit) {
        System.out.println(limit);
    }

    @DisplayName("ParameterizedTest with SimpleArgumentConverter")
    @ParameterizedTest(name = "{index} {displayName} limit={0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTestWithLimitToStudyConverter(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study);
    }

    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return Study.builder().limitCount(Integer.parseInt(source.toString())).build();
        }
    }

    @DisplayName("ParameterizedTest with ArgumentsAccessor")
    @ParameterizedTest(name = "{index} {displayName} limit={0}&name={1}")
    @CsvSource({"10, '자바 스터디'", "20, '스프링'"})
    void parameterizedTestWithArgumentAccessor(ArgumentsAccessor argumentsAccessor) {
        Integer limit = argumentsAccessor.getInteger(0);
        String name = argumentsAccessor.getString(1);
        System.out.println("limit = " + limit);
        System.out.println("name = " + name);
    }

    @DisplayName("ParameterizedTest with ArgumentsAggregator")
    @ParameterizedTest(name = "{index} {displayName} limit={0}&name={1}")
    @CsvSource({"10, '자바 스터디'", "20, '스프링'"})
    void parameterizedTestWithArgumentsAggregator(@AggregateWith(StudyArgumentsAggregator.class) Study study) {
        System.out.println("study = " + study);
    }

    static class StudyArgumentsAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return Study.builder()
                    .limitCount(accessor.getInteger(0))
                    .name(accessor.getString(1))
                    .build();
        }
    }

    @Test
    void extensionTest() throws Exception {
        Thread.sleep(1000L);
    }
}