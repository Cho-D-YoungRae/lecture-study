package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookQueryService(
    private val bookRepository: BookRepository,
) {

    fun search(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        return bookRepository.search(query, page, size)
    }
}