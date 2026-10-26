package hello.proxy.pureproxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class CacheProxy implements Subject {

    private final Subject target;

    private String cacheValue;

    @Override
    public String operation() {
        log.info("Call Proxy instance");
        if (isNull(cacheValue)) {
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}
