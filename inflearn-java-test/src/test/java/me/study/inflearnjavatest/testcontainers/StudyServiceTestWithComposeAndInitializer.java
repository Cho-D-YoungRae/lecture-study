package me.study.inflearnjavatest.testcontainers;

import me.study.inflearnjavatest.domain.Member;
import me.study.inflearnjavatest.domain.Study;
import me.study.inflearnjavatest.member.MemberService;
import me.study.inflearnjavatest.study.StudyRepository;
import me.study.inflearnjavatest.study.StudyService;
import me.study.inflearnjavatest.study.StudyStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
@ExtendWith({MockitoExtension.class})
@ActiveProfiles("test")
@Transactional
@Testcontainers
@ContextConfiguration(initializers = StudyServiceTestWithComposeAndInitializer.ContainerPropertyInitializer.class)
class StudyServiceTestWithComposeAndInitializer {

    @Mock
    MemberService memberService;

    @Autowired
    StudyRepository studyRepository;

    @Value("${container.port}") int port;

    @Container
    static DockerComposeContainer composeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("study-db", 5432);

    @BeforeEach
    void beforeAll() {
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
            TestPropertyValues.of(
                    "container.port=" + composeContainer.getServicePort("study-db", 5432))
                    .applyTo(applicationContext.getEnvironment());
        }
    }
}
