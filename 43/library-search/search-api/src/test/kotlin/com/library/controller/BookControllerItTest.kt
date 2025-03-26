package com.library.controller

import com.library.controller.request.SearchRequest
import com.library.controller.response.PageResult
import com.library.service.BookQueryService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerItTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var bookQueryService: BookQueryService

    @Test
    fun `정상 인자로 요청시 성공한다`() {
        // given
        val request = SearchRequest(
            query = "HTTP",
            page = 1,
            size = 10
        )

        given(bookQueryService.search(request.query, request.page, request.size))
            .willReturn(
                PageResult(
                    totalElements = 10,
                    page = 1,
                    size = 10,
                    contents = listOf()
                )
            )

        // when
        val result = mockMvc.perform(
            get("/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString())
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.totalElements").value(10))
            .andExpect(jsonPath("$.page").value(1))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.contents").isArray)
    }

    @Test
    fun `query가 비어있을 때 400 에러가 발생한다`() {
        // given
        val request = SearchRequest(
            query = "",
            page = 1,
            size = 10
        )

        // when
        val result = mockMvc.perform(
            get("/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString())
        )

        // then
        result
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.errorMessage").value("query : 입력은 비어있을 수 없습니다."))
    }


}