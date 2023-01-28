package com.example.batchtestpractice.ch1

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDate
import java.time.ZoneId

class CustomerRowMapper : RowMapper<Customer> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Customer =
        Customer(
            id = rs.getLong("id"),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            birthDate = LocalDate.ofInstant(
                rs.getDate("birth_date").toInstant(), ZoneId.systemDefault()
            )
        )
}