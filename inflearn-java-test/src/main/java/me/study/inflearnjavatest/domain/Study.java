package me.study.inflearnjavatest.domain;

import lombok.*;
import me.study.inflearnjavatest.study.StudyStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ToString
public class Study {

    private Long id;

    private StudyStatus status;

    private int limit;

    private String name;

    private LocalDateTime openDateTime;

    private Member owner;

    @Builder
    private Study(
            Long id,
            StudyStatus status,
            int limit,
            String name,
            Member owner) {
        this.id = id;

        this.status = Optional.ofNullable(status).orElse(StudyStatus.DRAFT);

        if (limit <= 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }
        this.limit = limit;

        this.name = name;

        this.owner = owner;
    }

    public void open() {
        this.openDateTime = LocalDateTime.now();
        this.status = StudyStatus.OPENED;
    }
}
