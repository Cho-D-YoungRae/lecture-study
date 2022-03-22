package study.datajpa2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa2.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
