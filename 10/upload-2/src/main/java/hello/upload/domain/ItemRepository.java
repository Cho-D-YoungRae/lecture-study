package hello.upload.domain;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepository {

    private final Map<Long, Item> store = new ConcurrentHashMap<>();

    private final AtomicLong sequence = new AtomicLong();

    public Item save(Item item) {
        item.setId(sequence.addAndGet(1));
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(long id) {
        return store.get(id);
    }
}
