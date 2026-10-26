package com.library.service

import com.library.repository.BookRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class BookQueryServiceTest {

    @Mock
    lateinit var bookRepository: BookRepository

    @InjectMocks
    lateinit var bookQueryService: BookQueryService

    @Test
    fun `search 시 인자가 그대로 넘어간다`() {
        // given
        val query = "test"
        val page = 1
        val size = 10

        // when
        bookQueryService.search(query, page, size)

        // then
        then(bookRepository).should().search(query, page, size)
    }
}