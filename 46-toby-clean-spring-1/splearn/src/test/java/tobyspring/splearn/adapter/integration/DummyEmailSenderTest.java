package tobyspring.splearn.adapter.integration;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import tobyspring.splearn.domain.shared.Email;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 콘솔 출력을 테스트 하는 법
 */
class DummyEmailSenderTest {

    @Test
    @StdIo
    void dummyEmailSender(StdOut out) {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("cho@splearn.app"), "subject", "body");

        assertThat(out.capturedLines()[0]).isEqualTo("Sending email: Email[address=cho@splearn.app]");
    }

}