package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.repository.BookRepository
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BookQueryService(
    private val naverBookRepository: BookRepository,
    private val kakaoBookRepository: BookRepository,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @CircuitBreaker(name = "naverSearch", fallbackMethod = "searchFallback")
    fun search(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        return naverBookRepository.search(query, page, size)
    }

    fun searchFallback(query: String, page: Int, size: Int, t: Throwable): PageResult<SearchResponse> {
        if (t is CallNotPermittedException) {
            return handleOpenCircuit(query, page, size)
        }
        return handleException(query, page, size)
    }

    private fun handleOpenCircuit(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        log.warn("Circuit breaker is open! Fallback to Kakao search")
        return kakaoBookRepository.search(query, page, size)
    }

    private fun handleException(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        log.error("An error occurred! Fallback to Kakao search")
        return kakaoBookRepository.search(query, page, size)
    }
}