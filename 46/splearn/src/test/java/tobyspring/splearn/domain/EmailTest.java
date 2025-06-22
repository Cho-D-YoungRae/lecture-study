package tobyspring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    void equality() {
        Email email1 = new Email("toby@splearn.app");
        Email email2 = new Email("toby@splearn.app");

        assertThat(email1).isEqualTo(email2);
    }

}