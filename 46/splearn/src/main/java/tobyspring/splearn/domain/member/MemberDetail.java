package tobyspring.splearn.domain.member;

import jakarta.persistence.Entity;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class MemberDetail extends AbstractEntity {

    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    protected MemberDetail() {
    }

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();
        return memberDetail;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getIntroduction() {
        return introduction;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public LocalDateTime getDeactivatedAt() {
        return deactivatedAt;
    }

    public void setActivatedAt() {
        Assert.isTrue(activatedAt == null, "이미 activatedAt이 설정되었습니다.");
        this.activatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        Assert.isTrue(deactivatedAt == null, "이미 deactivatedAt이 설정되었습니다.");
        this.deactivatedAt = LocalDateTime.now();
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.profile = new Profile(updateRequest.profileAddress());
        this.introduction = Objects.requireNonNull(updateRequest.introduction());
    }
}
