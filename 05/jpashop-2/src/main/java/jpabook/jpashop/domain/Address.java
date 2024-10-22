package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Address {

    private String city;

    private String street;

    private String zipcode;
}
