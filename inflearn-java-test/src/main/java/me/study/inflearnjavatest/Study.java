package me.study.inflearnjavatest;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@ToString
public class Study {

    private StudyStatus status;

    private int limit;

    private String name;

    @Builder
    public Study(StudyStatus status, int limit, String name) {
        this.status = Optional.ofNullable(status).orElse(StudyStatus.DRAFT);

        if (limit <= 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }
        this.limit = limit;

        this.name = name;
    }
}
