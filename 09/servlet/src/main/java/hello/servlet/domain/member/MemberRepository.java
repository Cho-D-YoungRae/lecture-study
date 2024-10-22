package hello.servlet.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRepository {

    // singleton 이므로 아래 두 변수가 static 아니어도 1개임이 보장된다.
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    private static final MemberRepository instance = new MemberRepository();

    public static MemberRepository getInstance() {
        return instance;
    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        // 반환된 리스트에 데이터를 추가하거나 삭제해도 원본 map 에 데이터가 추가되지 않도록 하기위해 아래와 같이 함.
        // 이렇게 해도 리스트 안의 데이터를 수정하면 반영됌
        // store 자체를 보호하기 위해
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
