package hello.proxy.pureproxy.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProxyPatterClient {

    private final Subject subject;

    public void execute() {
        subject.operation();
    }
}
