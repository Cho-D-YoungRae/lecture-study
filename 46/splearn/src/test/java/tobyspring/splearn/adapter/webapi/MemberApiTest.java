package tobyspring.splearn.adapter.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static tobyspring.splearn.AssertThatUtils.equalsTo;
import static tobyspring.splearn.AssertThatUtils.nonNull;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberApiTest {

    MockMvcTester mvcTester;
    ObjectMapper objectMapper;
    MemberRepository memberRepository;
    MemberRegister memberRegister;

    public MemberApiTest(
            MockMvcTester mvcTester,
            ObjectMapper objectMapper,
            MemberRepository memberRepository,
            MemberRegister memberRegister
    ) {
        this.mvcTester = mvcTester;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.memberRegister = memberRegister;
    }

    @Test
    void register() throws Exception {
        MemberRegisterRequest request = new MemberRegisterRequest(
                "cho@splearn.app",
                "choyr",
                "verysecret"
        );

        MvcTestResult result = mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", nonNull())
                .hasPathSatisfying("$.email", equalsTo(request.email()))
        ;

        var response = objectMapper.readValue(
                result.getResponse().getContentAsString(), MemberRegisterResponse.class);
        Member member = memberRepository.findById(response.memberId()).get();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail() throws Exception {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        MemberRegisterRequest request = new MemberRegisterRequest(
                "cho@splearn.app",
                "choyr",
                "verysecret"
        );

        MvcTestResult result = mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.CONFLICT);
    }
}