package me.study.inflearnjavatest.study;

import me.study.inflearnjavatest.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
