package com.library.repository

import com.library.NaverBookItemResponse
import com.library.NaverBookResponse
import com.library.feign.NaverClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class NaverBookRepositoryTest {

    @InjectMocks
    lateinit var bookRepository: NaverBookRepository
    @Mock
    lateinit var naverClient: NaverClient

    @Test
    fun `search 호출시 적절한 데이터형식으로 변환한다`() {
        // given
        val items = listOf(
            NaverBookItemResponse(
                title = "제목1",
                author = "저자1",
                publisher = "출판사1",
                pubDate = "20240101",
                isbn = "isbn1",
                description = "설명1",
                link = "link1",
                image = "image1",
                discount = "할인1"
            ),
            NaverBookItemResponse(
                title = "제목2",
                author = "저자2",
                publisher = "출판사2",
                pubDate = "20240102",
                isbn = "isbn2",
                description = "설명2",
                link = "link2",
                image = "image2",
                discount = "할인2"
            )
        )

        val response = NaverBookResponse(
            total = 2,
            start = 1,
            display = 2,
            lastBuildDate = "Wed, 29 May 2024 21:12:29 +0900",
            items = items
        )
        given(naverClient.search("query", 1, 2)).willReturn(response)

        // when
        val result = bookRepository.search("query", 1, 2)

        // then
        assertThat(result.page).isEqualTo(1)
        assertThat(result.size).isEqualTo(2)
        assertThat(result.totalElements).isEqualTo(2)
        assertThat(result.contents).hasSize(2)
        assertThat(result.contents[0].title).isEqualTo("제목1")
        assertThat(result.contents[0].author).isEqualTo("저자1")
        assertThat(result.contents[0].publisher).isEqualTo("출판사1")
        assertThat(result.contents[0].pubDate).isEqualTo(LocalDate.of(2024, 1, 1))
        assertThat(result.contents[0].isbn).isEqualTo("isbn1")
        assertThat(result.contents[1].title).isEqualTo("제목2")
        assertThat(result.contents[1].author).isEqualTo("저자2")
        assertThat(result.contents[1].publisher).isEqualTo("출판사2")
        assertThat(result.contents[1].pubDate).isEqualTo(LocalDate.of(2024, 1, 2))
        assertThat(result.contents[1].isbn).isEqualTo("isbn2")
    }
}