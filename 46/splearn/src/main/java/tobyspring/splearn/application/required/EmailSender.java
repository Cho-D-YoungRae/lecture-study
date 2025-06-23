package tobyspring.splearn.application.required;

import tobyspring.splearn.domain.Email;

public interface EmailSender {

    /**
     * 이메일을 발송한다.
     */
    void send(Email email, String subject, String body);
}
