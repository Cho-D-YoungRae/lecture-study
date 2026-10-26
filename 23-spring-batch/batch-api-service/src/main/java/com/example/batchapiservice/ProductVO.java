package com.example.batchapiservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {

    private Long id;

    private String name;

    private Integer price;

    private String type;
}
