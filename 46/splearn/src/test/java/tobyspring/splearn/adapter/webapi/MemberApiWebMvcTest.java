package tobyspring.splearn.adapter.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MemberApi.class)
class MemberApiWebMvcTest {

    @MockitoBean
    MemberRegister memberRegister;

    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void register() throws Exception {
        Member member = MemberFixture.createMember(1L);
        when(memberRegister.register(any())).thenReturn(member);
        MemberRegisterRequest request = new MemberRegisterRequest(
                "cho@splearn.app",
                "choyr",
                "verysecret"
        );

        assertThat(
                mvcTester.post().uri("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId").asNumber().isEqualTo(1);

        verify(memberRegister).register(request);
    }

    @Test
    void registerFail() throws Exception {
        MemberRegisterRequest request = new MemberRegisterRequest(
                "invalid email",
                "choyr",
                "verysecret"
        );

        assertThat(
                mvcTester.post().uri("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .hasStatus(HttpStatus.BAD_REQUEST);
    }
}