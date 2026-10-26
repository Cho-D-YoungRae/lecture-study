package com.example.batchpractice.ch15.batch.partition;

import com.example.batchpractice.ch15.batch.domain.ProductVO;
import com.example.batchpractice.ch15.batch.job.api.QueryGenerator;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ProductPartitioner implements Partitioner {

    private DataSource dataSource;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        ProductVO[] products = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();

        for (int i = 0; i < products.length; i++) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + i, value);
            value.put("product", products[i]);
        }

        return result;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
