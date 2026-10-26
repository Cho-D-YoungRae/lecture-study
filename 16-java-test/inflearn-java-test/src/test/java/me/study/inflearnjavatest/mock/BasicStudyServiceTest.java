package me.study.inflearnjavatest.mock;

import me.study.inflearnjavatest.member.MemberService;
import me.study.inflearnjavatest.study.StudyRepository;
import me.study.inflearnjavatest.study.StudyService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class BasicStudyServiceTest {

    @Test
    void createStudyService() {
        MemberService memberService = mock(MemberService.class);
        StudyRepository studyRepository = mock(StudyRepository.class);

        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();
    }
}