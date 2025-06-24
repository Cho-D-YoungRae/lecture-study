package tobyspring.splearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SplearnTestConfiguration.class);

    @Bean
    public EmailSender emailSender() {
        return (email, subject, body) -> {
            log.info("Sending email: {}", email);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
