package hello.proxy.pureproxy.concreteproxy.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConcreteClient {

    // ConcreteLogic, TimeProxy 모두 주입 가능
    private ConcreteLogic concreteLogic;

    public void execute() {
        concreteLogic.operation();
    }
}
