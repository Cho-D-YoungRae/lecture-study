package hello.proxy.jdkdynanmic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BImpl implements BInterface {

    @Override
    public String call() {
        log.info("Call B");
        return "b";
    }
}
