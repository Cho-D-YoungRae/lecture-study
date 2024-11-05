package annotation.mapping;

public class TestController {

    @SimpleMapping(value = "/")
    public void home() {
        System.out.println("TestController.home");
    }

    @SimpleMapping(value = "/page1")
    public void page1() {
        System.out.println("TestController.page1");
    }
}
