package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void 덧셈연산을_할_수_있다() {
        // given
        final long num1 = 2;
        final String operator = "+";
        final long num2 = 3;
        final Calculator calculator = new Calculator();

        // when
        final long result = calculator.calculate(num1, operator, num2);

        // then
        assertThat(result).isEqualTo(5);
    }

    @Test
    void 곱셈연산을_할_수_있다() {
        // given
        final long num1 = 2;
        final String operator = "*";
        final long num2 = 3;
        final Calculator calculator = new Calculator();

        // when
        final long result = calculator.calculate(num1, operator, num2);

        // then
        assertThat(result).isEqualTo(6);
    }

    @Test
    void 뺄셈연산을_할_수_있다() {
        // given
        final long num1 = 2;
        final String operator = "-";
        final long num2 = 3;
        final Calculator calculator = new Calculator();

        // when
        final long result = calculator.calculate(num1, operator, num2);

        // then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void 나눗셈연산을_할_수_있다() {
        // given
        final long num1 = 6;
        final String operator = "/";
        final long num2 = 3;
        final Calculator calculator = new Calculator();

        // when
        final long result = calculator.calculate(num1, operator, num2);

        // then
        assertThat(result).isEqualTo(2);
    }

    @Test
    void 잘못된_연산자가_요청으로_들어올_경우_에러가_난다() {
        // given
        final long num1 = 2;
        final String operator = "X";
        final long num2 = 3;
        final Calculator calculator = new Calculator();

        // when
        // then
        assertThatThrownBy(() -> calculator.calculate(num1, operator, num2))
                .isInstanceOf(InvalidOperatorException.class);
    }
}