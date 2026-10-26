package me.study.inflearnjavatest.testcontainers;

import me.study.inflearnjavatest.domain.Study;
import me.study.inflearnjavatest.study.StudyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;

    @Test
    void save() {
        studyRepository.deleteAll();
        Study study = Study.builder()
                .limitCount(10)
                .name("Java")
                .build();

        studyRepository.save(study);
        List<Study> all = studyRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }
}