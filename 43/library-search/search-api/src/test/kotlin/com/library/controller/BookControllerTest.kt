package com.library.controller

import com.library.service.BookApplicationService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class BookControllerTest {

    @Mock
    lateinit var bookApplicationService: BookApplicationService

    lateinit var bookController: BookController
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        bookController = BookController(bookApplicationService)
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build()
    }

    @Test
    fun search() {
        // given
        val query = "test"
        val page = 1
        val size = 10

        // when
        val response = mockMvc.perform(
            get("/v1/books")
                .param("query", query)
                .param("page", page.toString())
                .param("size", size.toString())
        ).andReturn().response

        // then
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        then(bookApplicationService).should().search(query, page, size)
    }
}