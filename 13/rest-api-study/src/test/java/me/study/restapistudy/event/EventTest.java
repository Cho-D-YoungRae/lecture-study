package me.study.restapistudy.event;

import junitparams.JUnitParamsRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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

    @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
//    @CsvSource({
//            "0, 0, true",
//            "100, 0, falae",
//            "0, 1000, falae"
//    })
    @MethodSource("parametersForTestFree")
    void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Object[] parametersForTestFree() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100, 0, false},
                new Object[]{0, 100, false},
                new Object[]{100, 200, false}

        };
    }

    @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
    @MethodSource("parametersForTestOffline")
    void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Object[] parametersForTestOffline() {
        return new Object[]{
                new Object[]{"강남역 네이버 D2 스타텁 팩토리", true},
                new Object[]{null, false},
                new Object[]{"    ", false}
        };
    }
}