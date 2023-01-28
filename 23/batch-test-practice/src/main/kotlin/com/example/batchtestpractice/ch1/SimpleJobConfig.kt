package com.example.batchtestpractice.ch1

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.support.H2PagingQueryProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class SimpleJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val dataSource: DataSource
) {

    @Bean
    fun job(
        step1: Step
    ) =
        jobBuilderFactory.get("batchJob")
            .incrementer(RunIdIncrementer())
            .start(step1)
            .build()

    @Bean
    fun step1(
        customerItemReader: ItemReader<Customer>,
        customerItemWriter: ItemWriter<Customer>
    ) =
        stepBuilderFactory.get("step1")
            .chunk<Customer, Customer>(100)
            .reader(customerItemReader)
            .writer(customerItemWriter)
            .build()

    @Bean
    fun customerItemReader(): ItemReader<Customer> {
        val reader = JdbcPagingItemReader<Customer>()

        reader.setDataSource(dataSource)
        reader.pageSize = 100
        reader.setRowMapper(CustomerRowMapper())

        val queryProvider = H2PagingQueryProvider()
        queryProvider.setSelectClause("id, first_name, last_name, birth_date")
        queryProvider.setFromClause("from customer")

        val sortKeys = mapOf(Pair("id", Order.ASCENDING))
        queryProvider.sortKeys = sortKeys

        reader.setQueryProvider(queryProvider)

        return reader
    }

    @Bean
    fun customerItemWriter(): ItemWriter<Customer> {
        val writer = JdbcBatchItemWriter<Customer>()

        writer.setDataSource(dataSource)
        writer.setSql("insert into customer_tmp values (:id, :firstName, :lastName, :birthDate")
        writer.setItemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
        writer.afterPropertiesSet()

        return writer
    }
}