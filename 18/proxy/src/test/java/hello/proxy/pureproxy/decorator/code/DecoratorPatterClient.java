package hello.proxy.pureproxy.decorator.code;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class DecoratorPatterClient {

    private Component component;

    public void execute() {
        String result = component.operation();
        log.info("result={}", result);
    }
}
