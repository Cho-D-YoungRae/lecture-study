package com.library.controller.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PageResultTest {

    @Test
    fun `객체가 생성된다`() {
        // given
        val page = 1
        val size = 10
        val totalElements = 100
        val contents = listOf("item1", "item2", "item3")

        // when
        val pageResult = PageResult(page, size, totalElements, contents)

        // then
        assertThat(pageResult.page).isEqualTo(page)
        assertThat(pageResult.size).isEqualTo(size)
        assertThat(pageResult.totalElements).isEqualTo(totalElements)
        assertThat(pageResult.contents).isEqualTo(contents)
    }
}