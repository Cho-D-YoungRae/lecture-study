package com.library.repository

import com.library.entity.DailyStat
import org.springframework.data.jpa.repository.JpaRepository

interface DailyStatRepository: JpaRepository<DailyStat, Long> {

}