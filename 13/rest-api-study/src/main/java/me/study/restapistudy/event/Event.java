package me.study.restapistudy.event;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.study.restapistudy.account.Account;
import me.study.restapistudy.account.AccountSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private LocalDateTime beginEnrollmentDateTime;

    private LocalDateTime closeEnrollmentDateTime;

    private LocalDateTime beginEventDateTime;

    private LocalDateTime endEventDateTime;

    private String location; // (optional) 이게 없으면 온라인 모임

    private int basePrice; // (optional)

    private int maxPrice; // (optional)

    private int limitOfEnrollment;

    private boolean offline;

    private boolean free;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        // Update free
        free = (basePrice == 0 && maxPrice == 0);

        offline = Objects.nonNull(location) && !location.isBlank();
    }
}
