package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    // 실무에서는 동시성 문제가 있을 수 있으므로 공유되는 변수이면
    // ConCurrentHashMap 을 사용해야 한다.
    private static Map<Long, Member> store = new HashMap<>();
    // 아래도 실무에서는 동시성을 고려해 AutomicLong 등...
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        // store 는 Map 으로 되어있지만, 반환은 List로 한다.
        // Loop 등 편하기 때문에
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
