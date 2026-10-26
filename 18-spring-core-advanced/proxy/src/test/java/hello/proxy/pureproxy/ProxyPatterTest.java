package hello.proxy.pureproxy;

import hello.proxy.pureproxy.code.CacheProxy;
import hello.proxy.pureproxy.code.ProxyPatterClient;
import hello.proxy.pureproxy.code.RealSubject;
import org.junit.jupiter.api.Test;

class ProxyPatterTest {

    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatterClient client = new ProxyPatterClient(realSubject);
        // 총 3초의 시간이 걸림
        client.execute();
        client.execute();
        client.execute();
    }

    @Test
    void cacheProxyTest() {
        RealSubject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        ProxyPatterClient client = new ProxyPatterClient(cacheProxy);
        // 처음에는 1초가 걸리고, 그 이후에는 캐싱을 통해 즉시 호출
        client.execute();
        client.execute();
        client.execute();
    }
}
