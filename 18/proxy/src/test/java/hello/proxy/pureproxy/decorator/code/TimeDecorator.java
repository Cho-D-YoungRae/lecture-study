package hello.proxy.pureproxy.decorator.code;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TimeDecorator implements Component {

    private Component component;

    @Override
    public String operation() {
        log.info("Call TimeDecorator");
        long startTime = System.currentTimeMillis();
        String result = component.operation();
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("End TimeDecorator resultTime={}", resultTime);
        return result;
    }
}
