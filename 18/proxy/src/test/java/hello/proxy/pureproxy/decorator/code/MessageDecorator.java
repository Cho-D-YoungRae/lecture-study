package hello.proxy.pureproxy.decorator.code;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MessageDecorator implements Component {

    private Component component;

    @Override
    public String operation() {
        log.info("Call MessageDecorator");

        String result = component.operation();
        String decoResult = "*****" + result + "*****";
        log.info("MessageDecorator applied before={}, after={}", result, decoResult);
        return decoResult;
    }
}
