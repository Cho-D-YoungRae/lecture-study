package me.study.restapistudy.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {


    @Test
    void builder() {
        Event event = Event.builder()
                .name("Spring REST API Lecture")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean() {
        Event event = new Event();
        String name = "Event";
        event.setName(name);
        String description = "Description";
        event.setDescription(description);

        assertThat(event.getName())
                .isEqualTo(name);
        assertThat(event.getDescription())
                .isEqualTo(description);
    }
}