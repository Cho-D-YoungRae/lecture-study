package network.tcp.autoclosable;

public class ResourceV1 {

    private final String name;

    public ResourceV1(String name) {
        this.name = name;
    }

    public void call() {
        System.out.println(name + " call");
    }

    public void callEx() throws CallException {
        System.out.println(name + " callEx");
        throw new CallException(name + " callEx");
    }

    public void close() {
        System.out.println(name + " close");
    }

    public void closeEx() throws CloseException {
        System.out.println(name + " closeEx");
        throw new CloseException(name + " closeEx");
    }
}
