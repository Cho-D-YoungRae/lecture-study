package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CalculationRequestReaderTest {

    @Test
    void System_in_으로_데이터를_읽어들일_수_있다() {
        // given
        final CalculationRequestReader calculationRequestReader = new CalculationRequestReader();

        // when
        System.setIn(new ByteArrayInputStream("2 + 3".getBytes()));
        final CalculationRequest result = calculationRequestReader.read();

        // then
        assertThat(result.num1()).isEqualTo(2);
        assertThat(result.operator()).isEqualTo("+");
        assertThat(result.num2()).isEqualTo(3);
    }

}