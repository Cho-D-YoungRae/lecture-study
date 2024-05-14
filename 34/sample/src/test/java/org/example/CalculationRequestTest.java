package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CalculationRequestTest {

    @Test
    void 유효한_숫자를_파싱할_수_있다() {
        // given
        final String[] parts = {"2", "+", "3"};

        // when
        final CalculationRequest calculationRequest = CalculationRequest.of(parts);

        // then
        assertThat(calculationRequest.num1()).isEqualTo(2);
        assertThat(calculationRequest.operator()).isEqualTo("+");
        assertThat(calculationRequest.num2()).isEqualTo(3);
    }

    @Test
    void 세자리_숫자가_넘어가는_유효한_숫자를_파싱할_수_있다() {
        // given
        final String[] parts = {"100", "+", "200"};

        // when
        final CalculationRequest calculationRequest = CalculationRequest.of(parts);

        // then
        assertThat(calculationRequest.num1()).isEqualTo(100);
        assertThat(calculationRequest.operator()).isEqualTo("+");
        assertThat(calculationRequest.num2()).isEqualTo(200);
    }

    @Test
    void 유효한_길이의_숫자가_들어오지_않으면_에러를_던진다() {
        // given
        final String[] parts = {"2", "+"};

        // when
        // then
        assertThatThrownBy(() -> CalculationRequest.of(parts))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 유효하지_않은_연산자가_들어오면_에러를_던진다() {
        // given
        final String[] parts = {"2", "x", "3"};

        // when
        // then
        assertThatThrownBy(() -> CalculationRequest.of(parts))
                .isInstanceOf(InvalidOperatorException.class);
    }

    @Test
    void 유효하지_않은_길이의_연산자가_들어오면_에러를_던진다() {
        // given
        final String[] parts = {"2", "++", "3"};

        // when
        // then
        assertThatThrownBy(() -> CalculationRequest.of(parts))
                .isInstanceOf(InvalidOperatorException.class);
    }


}