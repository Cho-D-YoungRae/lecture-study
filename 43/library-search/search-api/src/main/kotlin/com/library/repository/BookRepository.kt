package com.library.repository

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse

interface BookRepository {

    fun search(query: String, page: Int, size: Int): PageResult<SearchResponse>

}
