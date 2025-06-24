package tobyspring.splearn.domain;

import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.PasswordEncoder;

public class MemberFixture {

    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(
                email,
                "choyr",
                "verysecret"
        );
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("cho@splearn.app");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return password.toUpperCase().equals(passwordHash);
            }
        };
    }
}
