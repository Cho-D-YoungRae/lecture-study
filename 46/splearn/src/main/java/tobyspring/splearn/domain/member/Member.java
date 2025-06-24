package tobyspring.splearn.domain.member;

import jakarta.persistence.Entity;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.shared.Email;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@NaturalIdCache
public class Member extends AbstractEntity {

    @NaturalId
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private MemberDetail detail;

    protected Member() {
    }

    public static Member register(
            MemberRegisterRequest createRequest,
            PasswordEncoder passwordEncoder
    ) {
        Member member = new Member();
        member.email = new Email(createRequest.email());
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = passwordEncoder.encode(requireNonNull(createRequest.password()));

        member.status = MemberStatus.PENDING;

        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(this.status == MemberStatus.PENDING,
                MemberStatus.PENDING + " 상태가 아닙니다. 현재 상태: " + this.status);
        this.status = MemberStatus.ACTIVE;
        this.detail.setActivatedAt();
    }

    public void deactivate() {
        state(this.status == MemberStatus.ACTIVE,
                MemberStatus.ACTIVE + " 상태가 아닙니다. 현재 상태: " + this.status);

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.nickname = requireNonNull(updateRequest.nickname());
        this.detail.updateInfo(updateRequest);
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(newPassword));
    }

    public Email getEmail() {
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

    public MemberDetail getDetail() {
        return detail;
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

}
