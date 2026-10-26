package com.example.batchpractice.ch15.batch.job.api;

import com.example.batchpractice.ch15.batch.domain.ProductVO;
import com.example.batchpractice.ch15.batch.rowmapper.ProductRowMApper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryGenerator {

    public static ProductVO[] getProductList(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<ProductVO> products = jdbcTemplate.query("select type from product group by type", new ProductRowMApper() {
            @Override
            public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ProductVO.builder().type(rs.getString("type")).build();
            }
        });
        return products.toArray(new ProductVO[]{});
    }

    public static Map<String, Object> getParameterForQuery(String parameter, String value) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(parameter, value);
        return parameters;
    }
}
