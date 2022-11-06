package com.example.batchpractice.ch15.batch.job.api;

import com.example.batchpractice.ch15.batch.chunk.processor.ApiItemProcessor1;
import com.example.batchpractice.ch15.batch.chunk.processor.ApiItemProcessor2;
import com.example.batchpractice.ch15.batch.chunk.processor.ApiItemProcessor3;
import com.example.batchpractice.ch15.batch.chunk.writer.ApiItemWriter1;
import com.example.batchpractice.ch15.batch.chunk.writer.ApiItemWriter2;
import com.example.batchpractice.ch15.batch.chunk.writer.ApiItemWriter3;
import com.example.batchpractice.ch15.batch.classifier.ProcessorClassifier;
import com.example.batchpractice.ch15.batch.classifier.WriterClassifier;
import com.example.batchpractice.ch15.batch.domain.ApiRequestVO;
import com.example.batchpractice.ch15.batch.domain.ProductVO;
import com.example.batchpractice.ch15.batch.partition.ProductPartitioner;
import com.example.batchpractice.ch15.service.ApiService1;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfig {

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    private final ApiService1 apiService1;

    private final ApiService1 apiService2;

    private final ApiService1 apiService3;

    private int chunkSize = 10;

    @SneakyThrows
    @Bean
    public Step apiMasterStep() {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), partitioner())
                .step(apiSlaveStep())
                .gridSize(3)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setThreadNamePrefix("api-thread-");
        return taskExecutor;
    }

    private Step apiSlaveStep() throws Exception {
        return stepBuilderFactory.get("apiSlaveStep")
                .<ProductVO, ProductVO>chunk(chunkSize)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ProductPartitioner partitioner() {
        ProductPartitioner productPartitioner = new ProductPartitioner();
        productPartitioner.setDataSource(dataSource);
        return productPartitioner;
    }

    @Bean
    @StepScope
    public ItemReader<ProductVO> itemReader(@Value("#{stepExecutionContext['product']}") ProductVO productVO)
            throws Exception {
        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVO.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType()));
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemProcessor itemProcessor() {
        ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor = new ClassifierCompositeItemProcessor<>();
        ProcessorClassifier<? super ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> classifier =
                new ProcessorClassifier<>();
        classifier.setProcessorMap(Map.of(
                "1", new ApiItemProcessor1(),
                "2", new ApiItemProcessor2(),
                "3", new ApiItemProcessor3()
        ));
        processor.setClassifier(classifier);
        return processor;
    }

    @Bean
    public ItemWriter itemWriter() {
        ClassifierCompositeItemWriter<ApiRequestVO> processor = new ClassifierCompositeItemWriter<>();
        WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier = new WriterClassifier<>();
        classifier.setWriterMap(Map.of(
                "1", new ApiItemWriter1(apiService1),
                "2", new ApiItemWriter2(apiService2),
                "3", new ApiItemWriter3(apiService3)
        ));
        processor.setClassifier(classifier);
        return processor;
    }

}
