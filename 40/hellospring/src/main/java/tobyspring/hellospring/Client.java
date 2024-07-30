package tobyspring.hellospring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tobyspring.hellospring.exrate.ObjectFactory;
import tobyspring.hellospring.payment.PaymentService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(ObjectFactory.class);
        PaymentService paymentService = beanFactory.getBean(PaymentService.class);

        log.info("{}", paymentService.prepare(100L, "USD", new BigDecimal("50.7")));
        log.info("{}", paymentService.prepare(100L, "USD", new BigDecimal("50.7")));
        log.info("{}", paymentService.prepare(100L, "USD", new BigDecimal("50.7")));

        TimeUnit.SECONDS.sleep(4);

        log.info("{}", paymentService.prepare(100L, "USD", new BigDecimal("50.7")));
        log.info("{}", paymentService.prepare(100L, "USD", new BigDecimal("50.7")));
        log.info("{}", paymentService.prepare(100L, "USD", new BigDecimal("50.7")));
    }
}
