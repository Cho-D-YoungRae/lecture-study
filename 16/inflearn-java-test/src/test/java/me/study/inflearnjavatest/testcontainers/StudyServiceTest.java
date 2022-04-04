package me.study.inflearnjavatest.testcontainers;

import lombok.extern.slf4j.Slf4j;
import me.study.inflearnjavatest.domain.Member;
import me.study.inflearnjavatest.domain.Study;
import me.study.inflearnjavatest.member.MemberService;
import me.study.inflearnjavatest.study.StudyRepository;
import me.study.inflearnjavatest.study.StudyService;
import me.study.inflearnjavatest.study.StudyStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith({MockitoExtension.class})
@ActiveProfiles("test")
@Transactional
@Testcontainers
@Slf4j
// 컨테이너 정보를 스프링 테스트에서 사용하기 위해
@ContextConfiguration(initializers = StudyServiceTest.ContainerPropertyInitializer.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;

    @Autowired
    StudyRepository studyRepository;

    // ContainerPropertyInitializer 에서 넣어둔 값들이 담겨 있음
    @Autowired
    Environment environment;

    // Environment 에서 꺼내지 않고 바로 꺼낼 수 있다.
    @Value("${container.port}") int port;

    // 테스트를 실행할 때마다 컨테이너를 생성하면 테스트가 느려질 것
    // => static 으로 테스트들이 공유하도록 한다.
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("studytest");
    /*// TestContainers 에서 모듈로 제공하지 않는 이미지를 이용할 경우
    @Container
    static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_USER", "studytest")
            .withEnv("POSTGRES_PASSWORD", "studytest")
            .withEnv("POSTGRES_DB", "studytest");
    */
    @BeforeAll
    static void beforeAll() {
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
        postgreSQLContainer.followOutput(logConsumer);
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("=".repeat(30));
        System.out.println("postgreSQLContainer.getExposedPorts() = " + postgreSQLContainer.getExposedPorts());
        System.out.println(postgreSQLContainer.getLogs());
        System.out.println("environment.getProperty(\"container.port\") = " + environment.getProperty("container.port"));
        System.out.println("port = " + port);
    }

    @Test
    void createNewStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();

        Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");

        Study study = Study.builder()
                .limitCount(10)
                .name("테스트")
                .build();

        given(memberService.findById(member.getId())).willReturn(Optional.of(member));

        // When
        studyService.createNewStudy(member.getId(), study);

        // Then
        assertThat(member.getId()).isEqualTo(study.getOwnerId());
        then(memberService).should(times(1)).notify(study);
//        then(memberService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = Study.builder()
                .limitCount(10)
                .name("더 자바, 테스트")
                .build();
        assertThat(study.getOpenDateTime()).isNull();

        // When
        studyService.openStudy(study);

        // Then
        assertThat(study.getStatus()).isEqualTo(StudyStatus.OPENED);
        assertThat(study.getOpenDateTime()).isNotNull();
        then(memberService).should().notify(study);
    }

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("container.port=" + postgreSQLContainer.getMappedPort(5432))
                    .applyTo(applicationContext.getEnvironment());
        }
    }
}
