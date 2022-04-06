package me.study.inflearnjavatest.mock;

import me.study.inflearnjavatest.domain.Member;
import me.study.inflearnjavatest.domain.Study;
import me.study.inflearnjavatest.member.MemberService;
import me.study.inflearnjavatest.study.StudyRepository;
import me.study.inflearnjavatest.study.StudyService;
import me.study.inflearnjavatest.study.StudyStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;

    @Mock
    StudyRepository studyRepository;

    @Test
    void createStudyService() {
        Optional<Member> foundMember = memberService.findById(1L);
        assertThat(foundMember).isEmpty();
        memberService.validate(2L);  // void -> 아무것도 반환 X

        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();
    }

    @Test
    void simpleStubbing() {
        Member member = new Member();
        member.setId(1L);
        member.setEmail("youngrae@email.com");
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        Member foundMember = memberService.findById(1L).get();
        assertThat(foundMember.getId()).isEqualTo(member.getId());
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());

        doThrow(new IllegalArgumentException()).when(memberService).validate(2L);
        assertThatIllegalArgumentException().isThrownBy(() -> memberService.validate(2L));
    }

    @Test
    void 동일한_매개변수로_여러번_호출될_때_각기_다르게_행동하도록() {
        Member member = new Member();
        member.setId(1L);
        member.setEmail("youngrae@email.com");

        when(memberService.findById(any()))
                .thenReturn(Optional.of(member))
                .thenThrow(new IllegalArgumentException())
                .thenReturn(Optional.empty());

        assertThat(memberService.findById(any()).get().getId())
                .isEqualTo(member.getId());
        assertThatThrownBy(() -> memberService.findById(any()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(memberService.findById(any()))
                .isEmpty();
    }

    @Test
    void createNewStudy() {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();


        // TODO memberService 객체에 findById 메소드를 1L 값으로 호출하면 Optional.of(member) 객체를 리턴하도록 Stubbing
        Member member = new Member();
        member.setId(1L);
        member.setEmail("youngrae@email.com");
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        // TODO studyRepository 객체에 save 메소드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing
        Study study = Study.builder().limitCount(10).name("test").build();
        when(studyRepository.save(study)).thenReturn(study);

        Study newStudy = studyService.createNewStudy(1L, study);
        assertThat(newStudy).isNotNull();
        assertThat(newStudy.getOwnerId()).isEqualTo(member.getId());

        verify(memberService, times(1)).notify(study);
        verify(memberService, times(1)).notify(member);
        verify(memberService, never()).validate(any());
        verifyNoMoreInteractions(memberService);

        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(member);
    }

    @Test
    void createNewStudyWithBdd() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();

        Member member = new Member();
        member.setId(1L);
        member.setEmail("youngrae@email.com");
        given(memberService.findById(1L)).willReturn(Optional.of(member));

        Study study = Study.builder().limitCount(10).name("test").build();
        given(studyRepository.save(study)).willReturn(study);

        // When
        Study newStudy = studyService.createNewStudy(1L, study);

        // Then
        assertThat(newStudy).isNotNull();
        assertThat(newStudy.getOwnerId()).isEqualTo(member.getId());

        then(memberService).should(times(1)).notify(study);
        then(memberService).should(times(1)).notify(member);
        then(memberService).should(never()).validate(any());
        then(memberService).shouldHaveNoMoreInteractions();

        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(member);
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {

        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = Study.builder().limitCount(10).name("더 자바, 테스트").build();

        // TODO studyRepository Mock 객체의 save 메소드를호출 시 study를 리턴하도록 만들기.
        when(studyRepository.save(study)).thenReturn(study);

        // When
        Study openStudy = studyService.openStudy(study);

        // Then

        // TODO study의 status가 OPENED로 변경됐는지 확인
        assertThat(openStudy.getStatus()).isEqualTo(StudyStatus.OPENED);

        // TODO study의 openedDataTime이 null이 아닌지 확인
        assertThat(openStudy.getOpenDateTime()).isNotNull();

        // TODO memberService의 notify(study)가 호출 됐는지 확인.
        verify(memberService).notify(study);
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다. With BDD")
    @Test
    void openStudyWithBdd() {

        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = Study.builder().limitCount(10).name("더 자바, 테스트").build();

        // TODO studyRepository Mock 객체의 save 메소드를호출 시 study를 리턴하도록 만들기.
        given(studyRepository.save(study)).willReturn(study);

        // When
        Study openStudy = studyService.openStudy(study);

        // Then

        // TODO study의 status가 OPENED로 변경됐는지 확인
        assertThat(openStudy.getStatus()).isEqualTo(StudyStatus.OPENED);

        // TODO study의 openedDataTime이 null이 아닌지 확인
        assertThat(openStudy.getOpenDateTime()).isNotNull();

        // TODO memberService의 notify(study)가 호출 됐는지 확인.
        then(memberService).should().notify(study);
    }
}
