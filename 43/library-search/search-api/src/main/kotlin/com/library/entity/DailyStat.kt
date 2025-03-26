package com.library.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "daily_stat")
class DailyStat(
    @Column(name = "query") val query: String,
    @Column(name = "event_date_time") val eventDateTime: LocalDateTime,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}