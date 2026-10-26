package hello.proxy.pureproxy.concreteproxy.code;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TimeProxy extends ConcreteLogic {

    private ConcreteLogic concreteLogic;

    @Override
    public String operation() {
        log.info("Call TimeDecorator");
        long startTime = System.currentTimeMillis();

        String result = concreteLogic.operation();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("End TimeDecorator resultTime={}", resultTime);
        return result;
    }
}
