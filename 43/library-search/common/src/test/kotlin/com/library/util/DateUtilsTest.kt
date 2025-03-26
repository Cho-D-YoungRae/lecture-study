package com.library.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateUtilsTest {

    @Test
    fun `문자열(yyyyMMdd)로 날짜를 파싱한다`() {
        // given
        val dateString = "20231001"

        // when
        val date = parse(dateString)

        // then
        assertThat(date).isEqualTo(LocalDate.of(2023, 10, 1))
    }
}