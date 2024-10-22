package jpabook.jpashop.domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Book extends Item {

    private String author;

    private String isbn;
}
