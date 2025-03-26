package com.library.repository

import com.library.entity.DailyStat
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@ActiveProfiles("test")
@DataJpaTest
class DailyStatRepositoryTest {

    @Autowired
    lateinit var dailyStatRepository: DailyStatRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun `저장후 조회된다`() {
        // given
        val dailyStat = DailyStat(
            query = "test",
            eventDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0),
        )
        dailyStatRepository.saveAndFlush(dailyStat)

        // when
        entityManager.clear()
        val result = dailyStatRepository.findById(dailyStat.id!!)

        // then
        assertThat(result).isPresent()
        assertThat(result.get().query).isEqualTo(dailyStat.query)
        assertThat(result.get().eventDateTime).isEqualTo(dailyStat.eventDateTime)
        assertThat(result.get().id).isPositive()
    }
}