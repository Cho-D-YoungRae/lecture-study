package study.datajpa2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa2.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
