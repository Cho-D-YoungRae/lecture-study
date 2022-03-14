package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String city;

    private String street;

    private String zipcode;

    private DeliveryStatus status;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    private Order order;
}
