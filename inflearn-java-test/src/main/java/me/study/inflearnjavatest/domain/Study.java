package me.study.inflearnjavatest.domain;

import lombok.*;
import me.study.inflearnjavatest.study.StudyStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study {

    @Id @GeneratedValue
    private Long id;

    @Builder.Default
    private StudyStatus status = StudyStatus.DRAFT;

    private int limitCount;

    private String name;

    private LocalDateTime openDateTime;

    private Long ownerId;

    public Study(int limitCount) {
        if (limitCount <= 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }
        this.limitCount = limitCount;
        this.status = StudyStatus.DRAFT;
    }

    public void open() {
        this.openDateTime = LocalDateTime.now();
        this.status = StudyStatus.OPENED;
    }
}
