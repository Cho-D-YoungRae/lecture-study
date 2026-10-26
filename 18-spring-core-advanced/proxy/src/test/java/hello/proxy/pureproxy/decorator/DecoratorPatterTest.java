package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class DecoratorPatterTest {

    @Test
    void noDecorator() {
        Component realComponent = new RealComponent();
        DecoratorPatterClient client = new DecoratorPatterClient(realComponent);
        client.execute();
    }

    @Test
    void decorator1() {
        Component component = new RealComponent();
        Component messageDecorator = new MessageDecorator(component);
        DecoratorPatterClient client = new DecoratorPatterClient(messageDecorator);
        client.execute();
    }

    @Test
    void decorator2() {
        Component component = new RealComponent();
        Component messageDecorator = new MessageDecorator(component);
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatterClient client = new DecoratorPatterClient(timeDecorator);
        client.execute();
    }
}
