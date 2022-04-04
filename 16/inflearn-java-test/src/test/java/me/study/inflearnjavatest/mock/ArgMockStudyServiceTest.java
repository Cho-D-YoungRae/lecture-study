package me.study.inflearnjavatest.mock;

import me.study.inflearnjavatest.member.MemberService;
import me.study.inflearnjavatest.study.StudyRepository;
import me.study.inflearnjavatest.study.StudyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ArgMockStudyServiceTest {

    @Test
    void createStudyService(
            @Mock MemberService memberService,
            @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();
    }
}
