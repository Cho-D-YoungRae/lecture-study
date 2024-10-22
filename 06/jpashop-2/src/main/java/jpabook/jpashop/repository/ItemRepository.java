package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        // Item 은 처음에 데이터를 저장할 때는 id 가 없다.
        // 새로 생성한 객체다. -> JPA 저장하면서 id 가 생긴다.
        if (item.getId() == null) {
            em.persist(item);
        } else {
            // update 라고 생각. 일단은...
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
