package tobyspring.splearn.domain;

import java.util.Objects;

import static org.springframework.util.Assert.state;

public class Member {

    private String email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private Member(
            String email,
            String nickname,
            String passwordHash
    ) {
        this.email = Objects.requireNonNull(email);
        this.nickname = Objects.requireNonNull(nickname);
        this.passwordHash = Objects.requireNonNull(passwordHash);

        this.status = MemberStatus.PENDING;
    }

    public static Member create(
            String email,
            String nickname,
            String password,
            PasswordEncoder passwordEncoder
    ) {
        return new Member(
                email,
                nickname,
                passwordEncoder.encode(password)
        );
    }

    public void activate() {
        state(this.status == MemberStatus.PENDING,
                MemberStatus.PENDING + " 상태가 아닙니다. 현재 상태: " + this.status);
        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(this.status == MemberStatus.ACTIVE,
                MemberStatus.ACTIVE + " 상태가 아닙니다. 현재 상태: " + this.status);

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.passwordHash = Objects.requireNonNull(passwordEncoder.encode(newPassword));
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public MemberStatus getStatus() {
        return status;
    }
}
