package com.example.batchpractice.ch15.batch.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    private Long id;

    private String name;

    private Integer price;

    private String type;

    @Builder
    public Product(Long id, String name, Integer price, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }
}
