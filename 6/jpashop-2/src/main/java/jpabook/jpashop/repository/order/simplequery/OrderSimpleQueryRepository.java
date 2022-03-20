package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        // JPQL 에서 생성자를 호출할 때 Entity 를 넘겨줄 수 없다. -> 식별자가 넘어가게 된다.
        // 값 타입 Embedded Type 은 생성자에 넘겨줄 수 있다. -> ex) Address
        // 값을 각각 받아주는 생성자 필요
        return em.createQuery(
                "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
}
