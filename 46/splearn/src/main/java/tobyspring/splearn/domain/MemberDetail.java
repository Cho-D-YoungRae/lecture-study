package tobyspring.splearn.domain;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class MemberDetail extends AbstractEntity {

    private String profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    protected MemberDetail() {
    }
}
