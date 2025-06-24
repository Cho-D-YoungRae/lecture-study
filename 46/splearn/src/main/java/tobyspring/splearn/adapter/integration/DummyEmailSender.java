package tobyspring.splearn.adapter.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.Email;

@Component
public class DummyEmailSender implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(DummyEmailSender.class);

    @Override
    public void send(Email email, String subject, String body) {
        log.info("Sending email: {}", email);
    }
}
