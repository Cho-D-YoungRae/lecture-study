package com.library.repository

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.feign.NaverClient
import com.library.util.parse
import org.springframework.stereotype.Repository

@Repository
class NaverBookRepository(
    private val naverClient: NaverClient,
): BookRepository {

    override fun search(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        val response = naverClient.search(query, page, size)
        val responses = response.items.map {
            SearchResponse(
                title = it.title,
                author = it.author,
                publisher = it.publisher,
                pubDate = parse(it.pubDate),
                isbn = it.isbn,
            )
        }

        return PageResult(
            page = page,
            size = size,
            totalElements = response.total,
            contents = responses
        )
    }
}