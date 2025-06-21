package tobyspring.splearn.domain;

import static java.util.Objects.requireNonNull;
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
        this.email = requireNonNull(email);
        this.nickname = requireNonNull(nickname);
        this.passwordHash = requireNonNull(passwordHash);

        this.status = MemberStatus.PENDING;
    }

    private Member() {
    }

    /**
     * public static Member create(
     *             String email,
     *             String nickname,
     *             String password,
     *             PasswordEncoder passwordEncoder
     *     )
     * 비슷한 타입이 계속 연이어 나오게 되면 호출하는 쪽에서 혼란을 줄 수 있다.
     * 이후 순서가 변경되는 등의 상황에서 오류가 발생할 수 있다.
     *
     * 빌더를 사용할 수 있으나, 빌더는 누락된 파라미터를 컴파일 타임으로 체크할 수 없다.
     *
     * 파라미터 개수가 너무 길 때, 파라미터 오브젝트를 사용할 수 있다.
     * + 생성자를 사용하면 또 위와 같은 문제가 발생할 수 있는 것이므로 생성자를 지우고 아래 메서드에서 모두 해결한다.
     */
    public static Member create(
            MemberCreateRequest createRequest,
            PasswordEncoder passwordEncoder
    ) {
        Member member = new Member();
        member.email = requireNonNull(createRequest.email());
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = passwordEncoder.encode(requireNonNull(createRequest.password()));

        member.status = MemberStatus.PENDING;
        return member;
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
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(newPassword));
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

    /**
     * 회원의 정보를 바깥에서 사용하고 이를 통해 로직을 구현하게 됨
     * 예) 회원이 ACTIVE 이면, ~~ 한다 와 같이.
     * -> isActive() 와 같은 메서드를 만드는 것이 더 의미있을 수 있음.
     * 아래 메서드는 응집도가 낮아질 수 있음
     * 값을 꺼내는 것으로 게터들이 있지만 상태를 꺼내오는 것은 로직을 적용하기 위한 경우가 많음
     */
    public MemberStatus getStatus() {
        return status;
    }

    /**
     * isDeactivated() 와 같은 것도 다 만들어야 할까?
     * 이는 개발하면서 필요에 따라 추가하는 것이 좋다.
     * @return
     */
    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

}
