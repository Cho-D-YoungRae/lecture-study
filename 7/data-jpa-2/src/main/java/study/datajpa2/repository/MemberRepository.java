package study.datajpa2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa2.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
